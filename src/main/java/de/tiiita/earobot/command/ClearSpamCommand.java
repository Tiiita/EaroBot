package de.tiiita.earobot.command;

import de.tiiita.earobot.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on April 22, 2023 | 20:18:47
 * (●'◡'●)
 */
public class ClearSpamCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("clear-spam")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).submit();
            return;
        }

       final String messageTypeToDelete = event.getOption("message").getAsString();
        deleteMessages(event.getChannel(), messageTypeToDelete).whenComplete((deletedMessages, throwable) -> {
            if (deletedMessages.size() == 0) {
                event.replyEmbeds(EmbedUtil.getSimpleEmbed(Color.RED, "No messages like '**" + messageTypeToDelete + "**' found...")).setEphemeral(true).submit();
                return;
            }
            event.replyEmbeds(EmbedUtil.getSimpleEmbed(Color.WHITE, "Successfully removed **" + deletedMessages.size() + "** /**100** spam / raid messages!\n" +
                    "Repeat the command to check another 100 messages! \n\n_Discord allows only 100 checks in the same time..._\n\n" +
                    "Please note that this command is in alpha phase and is a little bug buggy!")).setEphemeral(true).submit();
        });
    }


    /**
     *
     * @param channel Channel where the code can get the history from.
     * @return CompletableFuture with a list of the last 100 messages
     *
     * Discord only allows 100 message in the same time, so that is why it returns "just" 100.
     */
    private CompletableFuture<List<Message>> getChannelMessages(MessageChannelUnion channel) {
        return channel.getHistory().retrievePast(100).submit();
    }

    /**
     *
     * @param channel The channel with the messages in.
     * @param sameMessage All message that are ".equalsIgnoreCase" with the sameMessage will be deleted. (Just last 100 checked)
     * @return A list of all deleted messages
     */
    private CompletableFuture<List<Message>> deleteMessages(MessageChannelUnion channel, String sameMessage) {
        return getChannelMessages(channel).thenApply(messages -> {
            List<Message> deletedMessages = new ArrayList<>();
            for (Message message : messages) {
                if (message.getContentStripped().equalsIgnoreCase(sameMessage)) {
                    deletedMessages.add(message);
                    message.delete().submit();
                }
            }
            return deletedMessages;
        });
    }
}
