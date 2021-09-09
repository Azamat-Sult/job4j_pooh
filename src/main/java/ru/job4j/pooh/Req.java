package ru.job4j.pooh;

public class Req {
    private final String method;
    private final String mode;
    private final String text;

    private Req(String method, String mode, String text) {
        this.method = method;
        this.mode = mode;
        this.text = text;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        String[] firstLine = lines[0].split(" ");
        String parsedMethod = firstLine[0];
        String[] modeNameId = firstLine[1].split("/");
        String parsedMode = modeNameId[1];
        String parsedText = "";
        if (parsedMethod.equals("POST")) {
            parsedText = modeNameId[2] + "/" + lines[lines.length - 1];
        }
        if (parsedMethod.equals("GET") && parsedMode.equals("queue")) {
            parsedText = modeNameId[2];
        }
        if (parsedMethod.equals("GET") && parsedMode.equals("topic")) {
            parsedText = modeNameId[2] + "/" + modeNameId[3];
        }
        return new Req(parsedMethod, parsedMode, parsedText);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }
}
