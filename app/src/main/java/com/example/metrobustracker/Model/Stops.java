package com.example.metrobustracker.Model;

public class Stops {

    String Text;
    String Value;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = value;
    }

    @Override
    public String toString() {
        return "Stops{" +
                "Text='" + Text + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}
