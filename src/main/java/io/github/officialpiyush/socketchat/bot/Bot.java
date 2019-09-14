package io.github.officialpiyush.socketchat.bot;

import io.github.officialpiyush.socketchat.utils.env;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import io.github.officialpiyush.socketchat.utils.SocketHandler;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;
import java.util.Optional;

public class Bot extends ListenerAdapter {
    public static void main(String[] args) throws LoginException, InterruptedException {
        try {
            new JDABuilder(env.get("BOT_TOKEN"))
                    .addEventListeners(new Bot())
                    .setActivity(Activity.listening("webscockets"))
                    .build()
                    .awaitReady();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("UwU");
    }


    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("[ DISCORD ] Bot Ready!");

        try {
            SocketHandler s = new SocketHandler(event, true, Optional.empty(), Optional.empty(), Optional.empty());
            s.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals(env.get("DISCORD_CHANNEL_ID")) && !event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            try {
                SocketHandler s = new SocketHandler(event, false, Optional.of(event.getMessage().getContentRaw()), Optional.of(event.getAuthor().getAsTag()), Optional.of(event.getAuthor().getAvatarUrl()));
                s.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
