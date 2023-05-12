package com.vexsoftware.votifier.folia;

import com.vexsoftware.votifier.NuVotifierBukkit;
import com.vexsoftware.votifier.platform.scheduler.VotifierScheduler;

public class NuVotifierFolia extends NuVotifierBukkit {
    private static final boolean FOLIA_SUPPORTED;

    static {
        boolean foliaSupported = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            foliaSupported = true;
        } catch (ClassNotFoundException e) {
            // Ignore
        }
        FOLIA_SUPPORTED = foliaSupported;
    }

    @Override
    protected VotifierScheduler loadScheduler() {
        if (FOLIA_SUPPORTED) {
            return new FoliaScheduler(this);
        } else {
            return super.loadScheduler();
        }
    }
}
