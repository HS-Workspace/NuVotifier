package com.vexsoftware.votifier.folia;

import com.vexsoftware.votifier.NuVotifierBukkit;
import com.vexsoftware.votifier.platform.scheduler.ScheduledVotifierTask;
import com.vexsoftware.votifier.platform.scheduler.VotifierScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaScheduler implements VotifierScheduler {
    private final NuVotifierBukkit plugin;

    public FoliaScheduler(NuVotifierBukkit plugin) {
        this.plugin = plugin;
    }

    private int toTicks(int time, TimeUnit unit) {
        return (int) (unit.toMillis(time) / 50);
    }

    private Consumer<ScheduledTask> wrap(Runnable runnable) {
        return (task) -> runnable.run();
    }

    private ScheduledVotifierTask wrap(ScheduledTask task) {
        return task::cancel;
    }

    @Override
    public ScheduledVotifierTask sync(Runnable runnable) {
        return wrap(plugin.getServer().getGlobalRegionScheduler().run(plugin, wrap(runnable)));
    }

    @Override
    public ScheduledVotifierTask onPool(Runnable runnable) {
        return wrap(plugin.getServer().getAsyncScheduler().runNow(plugin, wrap(runnable)));
    }

    @Override
    public ScheduledVotifierTask delayedSync(Runnable runnable, int delay, TimeUnit unit) {
        return wrap(plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, wrap(runnable), toTicks(delay, unit)));
    }

    @Override
    public ScheduledVotifierTask delayedOnPool(Runnable runnable, int delay, TimeUnit unit) {
        return wrap(plugin.getServer().getAsyncScheduler().runDelayed(plugin, wrap(runnable), delay, unit));
    }

    @Override
    public ScheduledVotifierTask repeatOnPool(Runnable runnable, int delay, int repeat, TimeUnit unit) {
        return wrap(plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, wrap(runnable), delay, repeat, unit));
    }
}
