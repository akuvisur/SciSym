package com.akuvisuri.scisym.view.tracking;

import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Trackable;

/**
 * Created by Aku on 13.7.2015.
 */
public class FactorTracked implements Tracking {
    private Factor f;
    public FactorTracked(Factor _f) {
        super();
        f = _f;
    }

    @Override
    public Trackable getTrackable() {
        return f;
    }
}
