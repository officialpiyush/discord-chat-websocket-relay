package io.github.officialpiyush.socketchat.utils;

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

    public SocketHandler(GenericEvent discordEvent, Boolean toReply, Optional<String> message) throws URISyntaxException {
        super(new URI(env.get("WEBSOCKET_URI")));
        this.event = discordEvent;
        this.toReply = toReply;
        this.message = message.isPresent() ? message.get() : null;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (this.toReply) {
            TextChannel channel = this.event.getJDA().getTextChannelById(Long.parseLong(env.get("DISCORD_CHANNEL_ID")));
            send("[Connection] Established - socket-chat backend");

            channel.sendMessage("[Connection] Established - socket-chat backend").queue();
        } else {
            send(this.message);
        }
    }

    @Override
    public void onMessage(String message) {
        if (this.toReply && !message.startsWith("Discord:")) {
            TextChannel channel = this.event.getJDA().getTextChannelById(Long.parseLong(env.get("DISCORD_CHANNEL_ID")));

            channel.sendMessage(message).queue();
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

    public void sendData(String message) {
        send(message);
    }
}