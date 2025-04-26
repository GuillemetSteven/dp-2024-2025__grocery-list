package com.fges.parser;

import com.fges.commands.Command;
import java.util.List;

public class ParsingResult {

    private Command command;
    private List<String> positionalArgs;
    private String sourceFile;
    private String format;
    private String category;
    private boolean categorySpecified; // true si -c a été passé explicitement

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public List<String> getPositionalArgs() {
        return positionalArgs;
    }

    public void setPositionalArgs(List<String> positionalArgs) {
        this.positionalArgs = positionalArgs;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCategorySpecified() {
        return categorySpecified;
    }

    public void setCategorySpecified(boolean categorySpecified) {
        this.categorySpecified = categorySpecified;
    }
}
