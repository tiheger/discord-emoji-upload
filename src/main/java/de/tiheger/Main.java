package de.tiheger;

import de.tiheger.exception.DeletionException;
import de.tiheger.exception.UploadException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Icon;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Main {
    private static final String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
    private static final String EMOJI_PATH = System.getenv("EMOJI_PATH");

    public static void main(String[] args) {
        try {
            JDA jda = JDABuilder.createDefault(DISCORD_BOT_TOKEN)
                    .build()
                    .awaitReady();

            //deleteOldEmojis(jda);
            uploadNewEmojis(jda);

            jda.shutdown();
            jda.awaitShutdown();
            System.out.println("Tasks done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadNewEmojis(JDA jda) throws UploadException {
        File path = new File(EMOJI_PATH);
        List<File> files = Stream.of(Objects.requireNonNull(path.listFiles()))
                .sorted(Comparator.comparingInt((File file) -> file.getName().length())
                        .thenComparing(File::getName))
                .toList();

        System.out.println("Found " + files.size() + " emojis");

        files.forEach(file -> {
            String emojiName = file.getName().replace(".png", "");
            try {
                jda.createApplicationEmoji(emojiName, Icon.from(file)).complete();
                System.out.println("Uploaded " + emojiName);
            } catch (Exception e) {
                throw new UploadException(e);
            }
        });
    }

    private static void deleteOldEmojis(JDA jda) throws DeletionException {
        jda.retrieveApplicationEmojis().complete()
                .forEach(emoji -> {
                    try {
                        emoji.delete().complete();
                        System.out.println("Deleted " + emoji.getName());
                    } catch (Exception e) {
                        throw new DeletionException(e);
                    }
                });
    }
}