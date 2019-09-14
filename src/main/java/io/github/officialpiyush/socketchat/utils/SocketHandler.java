package io.github.officialpiyush.socketchat.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class SocketHandler extends WebSocketClient {
    private final GenericEvent event;
    private final Boolean toReply;
    private final String message;
    private final String url;
    private final String tag;

    public SocketHandler(GenericEvent discordEvent, Boolean toReply, Optional<String> message, Optional<String> tag, Optional<String> url) throws URISyntaxException {
        super(new URI(env.get("WEBSOCKET_URI")));
        this.event = discordEvent;
        this.toReply = toReply;
        this.message = message.isPresent() ? message.get() : null;
        this.tag = tag.isPresent() ? tag.get() : null;
        this.url = url.isPresent() ? url.get() : null;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (this.toReply) {
            TextChannel channel = this.event.getJDA().getTextChannelById(Long.parseLong(env.get("DISCORD_CHANNEL_ID")));
            send("[Connection] Established - socket-chat backend");

            channel.sendMessage("[Connection] Established - socket-chat backend").queue();
        } else {
            JsonObject response_data = new JsonObject();
            response_data.addProperty("isFromDiscord", true);
            response_data.addProperty("user", this.tag);
            response_data.addProperty("avatar", this.url);
            response_data.addProperty("message", this.message);
            send(response_data.toString());
        }
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonObject message_object = new JsonParser().parse(message).getAsJsonObject();
            Boolean isFromDiscord = message_object.get("isFromDiscord").getAsBoolean();
            String msg = message_object.get("message").getAsString();

            if (this.toReply && !isFromDiscord) {
                TextChannel channel = this.event.getJDA().getTextChannelById(Long.parseLong(env.get("DISCORD_CHANNEL_ID")));

                channel.sendMessage(msg).queue();
            }
        } catch (IllegalStateException e) {
            // Nah idc about broken requests
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (this.toReply) {
            TextChannel channel = this.event.getJDA().getTextChannelById(Long.parseLong(env.get("DISCORD_CHANNEL_ID")));

            channel.sendMessage("[CONNECTION] Closed").queue();
        }
    }

    @Override
    public void onError(Exception ex) {
        if (this.toReply) {
            TextChannel channel = this.event.getJDA().getTextChannelById(Long.parseLong(env.get("DISCORD_CHANNEL_ID")));

            channel.sendMessage("[Websocket] Exception ```" + ex.toString() + "```").queue();
        }
    }
}