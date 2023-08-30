/*
package com.example.webshop.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

class CancelScheduledTask<T> implements Runnable
{
    private ScheduledExecutorService scheduledExecutorServiceRunningThisTask;
    private ExecutorService cancellationExecutorService;
    private Supplier<T> task;
    public CancelScheduledTask (final ScheduledExecutorService scheduledExecutorServiceRunningThisTask , final ExecutorService cancellationExecutorService,
                                final Supplier<T> task)
    {
        this.scheduledExecutorServiceRunningThisTask = Objects.requireNonNull( scheduledExecutorServiceRunningThisTask );
        this.cancellationExecutorService = Objects.requireNonNull( cancellationExecutorService );
        this.task=task;
    }

    @Override
    public void run ( )
    {
        if ( this.task.get().equals(0) )
        {
            this.cancellationExecutorService.submit( ( ) -> this.scheduledExecutorServiceRunningThisTask.shutdown() );
        }
        return null;
    }
}
*/
