package com.akuvisuri.scisym.view.tracking;

import com.akuvisuri.scisym.trackables.Symptom;
import com.akuvisuri.scisym.trackables.Trackable;

/**
 * Created by Aku on 20.7.2015.
 */
public class SymptomScale implements Tracking {
    private Symptom s;
    public SymptomScale(Symptom _s) {
        super();
        s = _s;
    }

    @Override
    public Trackable getTrackable() {
        return s;
    }
}
