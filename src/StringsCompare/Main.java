package StringsCompare;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@SuppressWarnings("NonAsciiCharacters")
public class Main {
    static String FILE_NAME = "input.txt";

    /**
     * A list of synonym words that may occur in the file.
     * can be changed and supplemented via static
     */
    static List<List<String>> synonyms;
    static {
        synonyms = new ArrayList<>();
        synonyms.add(List.of("гвоздь", "кол", "жердь", "гвоздик", "штырь", "стержень", "крепеж", "крепление"));
        synonyms.add(List.of("шуруп", "винт", "шурупчик", "саморез", "винтик", "болт"));
        synonyms.add(List.of("краска", "гуашь", "окраска", "акварель", "покрытие", "колер"));
        synonyms.add(List.of("ведро", "сосуд", "корзина", "емкость","коробка", "короб", "бочка", "бадья", "кадка", "тара"));
        synonyms.add(List.of("бетон", "шлакобетон", "металлопластик", "пенобетон", "цемент", "гранит", "асфальт", "асфальтобетон", "камень", "стеклоблок", "бетонка"));
    }

    public static void main(String[] args) {
        List<String> inputText = readFile(FILE_NAME);

        List<String> nSubstrings = inputText.subList(1, Integer.parseInt(inputText.get(0)) + 1);
        List<String> mSubstrings = inputText.subList(Integer.parseInt(inputText.get(0)) + 2, inputText.size());

        List<String> outputText = compareLists(nSubstrings, mSubstrings);

        writeFile(outputText);

    }

    /**
     * method creates a new file and writes to it the result of string comparison
     * @param outputText result of string comparison
     */
    private static void writeFile(@NotNull List<String> outputText) {
        try {
            File file = new File("output.txt");
            Files.write(file.toPath(), outputText.stream().
                    map(StringBuffer::new).collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to read the contents of a file using the BufferedReader
     * @param fileName is taking from main method
     * @return List<String> contains all information from current file
     */
    private static @NotNull List<String> readFile(String fileName) {
        List<String> inputText = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(inputText::add);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return inputText;
    }


    /**
     * method for comparing strings from two lists
     * @param nSubstrings received from the main method
     * @param mSubstrings received from the main method
     * @return a new list consisting of matched strings
     */
    public static @NotNull List<String> compareLists(@NotNull List<String> nSubstrings, List<String> mSubstrings) {
        List<String> comparedList = new ArrayList<>();

        for (String nStr : nSubstrings) {
            boolean isContains = false;
            String result = "";
            for (String mStr : mSubstrings) {
                for (String element : mStr.split(" ")) {
                    if (element.length() < 3) {
                        continue;
                    }
                    if (nStr.toLowerCase().contains(element.toLowerCase().substring(0, element.length() - 1))) {
                        isContains = true;
                        break;
                    }
                }
                if (isContains || isMeaningSimilar(nStr, mStr)) {
                    result = nStr + " : " + mStr;
                    isContains = true;
                    break;
                }
            }
            if (isContains) {
                comparedList.add(result);
            } else comparedList.add(nStr + " : ?");
        }
        if (nSubstrings.size() < mSubstrings.size()) {
            int count = nSubstrings.size();
            for (int i = count; i < mSubstrings.size(); i++) {
                if (!comparedList.contains(mSubstrings.get(i))) {
                    comparedList.add(mSubstrings.get(i) + " : ?");
                }
            }
        }
        return comparedList;
    }

    /**
     * method checking for matches of words from subsets with the above created database of synonyms
     * @param nStr phrase or word from nSubstrings subset
     * @param mStr phrase or word from mSubstrings subset
     * @return true if both words are in the database
     */
    private static boolean isMeaningSimilar(String nStr, String mStr) {
        boolean resultOfChecking = false;

        for (List<String> groupOfSynonyms : synonyms) {
            AtomicBoolean containsInFirs = new AtomicBoolean(false);
            AtomicBoolean containsInSecond = new AtomicBoolean(false);

            for (String s : groupOfSynonyms) {
                if (nStr.toLowerCase().contains(s)) {
                    containsInFirs.set(true);
                    break;
                }
            }

            for (String s : groupOfSynonyms) {
                if (mStr.toLowerCase().contains(s)) {
                    containsInSecond.set(true);
                    break;
                }
            }

            if (containsInFirs.get() && containsInSecond.get()) {
                resultOfChecking = true;
                break;
            }
        }
        return resultOfChecking;
    }

}
