import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Project {
    public static void main(String[] args) throws IOException{
        ArrayList<SentenceBS> sentenceContainer = new ArrayList<>();
        ArrayList<String> sentences = new ArrayList<>();
        String content = "";
        content = Files.readString(Paths.get(args[0]));
        content = content.trim() + " ";
        String regex = "(?<!(Mr)|(Ms)|(Mrs)|(Dr)|(apt)|(Ave)|(St)|(Rd)|(Ln)|(Cyn)|(Blvd)|(vs)|(Jan)|(Feb)|(Sept)|(Oct)|(Nov)|(Dec))(?<=([A-Za-z%'\"\\)]{2,})|( )|(\\d)|(\\ds)|(\\d%))(\\.{4}((\"|”)\\.?|\\)\\.?)?|(\\.((\"|”)\\.?|\\)\\.?)?|\\?((\"|”)(\\.|\\))?|\\)\\.?)?|\\!((\"|”)\\.?|\\)\\.?)?))(?!(( [,a-z]+)|([.]{2})|(\\d)|(,)|(')|( ')))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            sentenceContainer.add(new SentenceBS(matcher.start(), matcher.end()));
        }
        String sentence = "";
        StringBuilder stringBuilder= new StringBuilder(sentence);
        int index = 0;
        int sentenceNumber = 0;
        boolean quoted = false;
        while(content.length()>index){
            try {
                if (sentenceContainer.get(sentenceNumber).getBeginning() > index) {
                    char contentLoc = content.charAt(index);
                    stringBuilder.append(contentLoc);
                    if(Character.toString(contentLoc).equals("\"") | Character.toString(contentLoc).equals("“")| Character.toString(contentLoc).equals("”"))
                         if(quoted==true){
                             quoted=false;
                          }
                         else {
                            quoted = true;
                         }
                        index++;

                }
                else{
                    for(int i = sentenceContainer.get(sentenceNumber).getBeginning(); i < sentenceContainer.get(sentenceNumber).getEnd(); i++){
                        stringBuilder.append(content.charAt(i));
                        char contentLoc = content.charAt(index);
                        if(Character.toString(contentLoc).equals("\"") | Character.toString(contentLoc).equals("”"))
                            if (quoted == true) {
                                quoted = false;
                            } else {
                                quoted = true;
                            }
                            index++;

                    }
                    if(quoted==true){
                        sentenceNumber++;
                    }
                    else {
                        sentences.add(stringBuilder.toString());
                        stringBuilder.replace(0, stringBuilder.length(), "");
                        sentenceNumber++;
                    }
                }
            }catch (IndexOutOfBoundsException e){
                break;
            }
        }

        int start_Index = args[0].indexOf(".txt");
        String location = args[0].substring(0,start_Index) + "Split.txt";

        try{
            PrintWriter writer = new PrintWriter(location, "UTF-8");

            for(int i =0; i < sentences.size(); i++){
                writer.println("Sentence Number " + i + ": " + sentences.get(i) + "\n");
                System.out.println("Sentence Number " + i + ": " + sentences.get(i) + "\n");
            }
            writer.close();
        }catch (FileNotFoundException | UnsupportedEncodingException e){
            e.getMessage();
        }
    }
}
