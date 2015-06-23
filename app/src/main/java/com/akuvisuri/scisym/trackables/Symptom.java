package com.akuvisuri.scisym.trackables;

/**
 * Created by Aku on 22.6.2015.
 */
public class Symptom {
    protected String label;
    protected String desc;
    protected String _class;
    protected Integer severity;

    public Symptom(String label, String desc, String _class, Integer severity) {
        this.label = label;
        this.desc = desc;
        this._class = _class;
        this.severity = severity;
    }

    public String toString() {
        return this.label;
    }

    public String attrToString() {
        return this._class + ", " + this.desc;
    }

    public boolean isSevere() {
        return severity.equals(1);
    }
}
