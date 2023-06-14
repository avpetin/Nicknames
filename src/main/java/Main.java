import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger count_3 = new AtomicInteger(0);
    public static AtomicInteger count_4 = new AtomicInteger(0);
    public static AtomicInteger count_5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread[] threads = new Thread[3];
        threads[0] = new Thread(() -> {
            for (String text : texts) {
                String revStr = new StringBuilder(text).reverse().toString();
                if (text.equals(revStr)) {
                    incrementCounts(text.length());
                }
            }
        });

        threads[1] = new Thread(() -> {
            for (String text : texts) {
                int j;
                for (j = 1; j < text.length(); j++) {
                    if (text.charAt(j) != text.charAt(0)) {
                        break;
                    }
                }
                if (j == text.length()) {
                    incrementCounts(text.length());
                }
            }
        });

        threads[2] = new Thread(() -> {
            for (String text : texts) {
                char ch = text.charAt(0);
                int j;
                for (j = 1; j < text.length(); j++) {
                    if (ch < text.charAt(j)) {
                        ch = text.charAt(j);
                    } else if (ch > text.charAt(j)) {
                        break;
                    } else if (j == text.length() - 1) {
                        break;
                    }
                }
                if (j == text.length()) {
                    incrementCounts(text.length());
                }
            }
        });

        threads[0].start();
        threads[1].start();
        threads[2].start();

        threads[0].join();
        threads[1].join();
        threads[2].join();

        System.out.println("Красивых слов с длиной 3: " + count_3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + count_4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + count_5.get() + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void incrementCounts(int length) {
        switch (length) {
            case 3 -> count_3.incrementAndGet();
            case 4 -> count_4.incrementAndGet();
            case 5 -> count_5.incrementAndGet();
        }
    }
}