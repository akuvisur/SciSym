package com.akuvisuri.scisym.view.tracking;

import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Trackable;

/**
 * Created by Aku on 13.7.2015.
 */
public class FactorMultiple implements Tracking {
    private Factor f;
    public FactorMultiple(Factor _f) {
        super();
        f = _f;
    }

    @Override
    public Trackable getTrackable() {
        return f;
    }
}
