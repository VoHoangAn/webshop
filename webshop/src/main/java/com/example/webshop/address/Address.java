package com.example.webshop.address;

import com.example.webshop.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String street;
    private String province;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String city;
    @CreationTimestamp
    private Timestamp timestamp;
//    @Column(nullable = false)
//    private Long userId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnore
    private User user;
}

/*package work.basil.tasking;

        import java.time.Duration;
        import java.time.Instant;
        import java.util.Objects;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.ScheduledExecutorService;
        import java.util.concurrent.TimeUnit;

public class App
{
    public static void main ( String[] args )
    {
        App app = new App();
        app.demo();
    }

    private void demo ( )
    {
        ScheduledExecutorService coreScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ExecutorService cancellationExecutorService = Executors.newSingleThreadExecutor();
        Duration expires = Duration.ofMinutes( 2 );
        Runnable coreTask = new CoreTask( expires , coreScheduledExecutorService , cancellationExecutorService );
        coreScheduledExecutorService.scheduleAtFixedRate( Objects.requireNonNull( coreTask ) , 0 , 20 , TimeUnit.SECONDS );

        try { Thread.sleep( expires.plus( Duration.ofMinutes( 1 ) ).toMillis() ); } catch ( InterruptedException e ) { e.printStackTrace(); }
        if ( Objects.nonNull( coreScheduledExecutorService ) )
        {
            if ( ! coreScheduledExecutorService.isShutdown() )
            {
                coreScheduledExecutorService.shutdown();
                try { coreScheduledExecutorService.awaitTermination( 1 , TimeUnit.MINUTES ); } catch ( InterruptedException e ) { e.printStackTrace(); }
            }
        }
        if ( Objects.nonNull( cancellationExecutorService ) )
        {
            if ( ! cancellationExecutorService.isShutdown() )
            {
                cancellationExecutorService.shutdown();
                try { cancellationExecutorService.awaitTermination( 1 , TimeUnit.MINUTES ); } catch ( InterruptedException e ) { e.printStackTrace(); }
            }
        }
    }

    class CoreTask implements Runnable
    {
        private ScheduledExecutorService scheduledExecutorServiceRunningThisTask;
        private ExecutorService cancellationExecutorService;
        private Duration exiration;
        Instant whenCreated;

        public CoreTask ( final Duration expiration , final ScheduledExecutorService scheduledExecutorServiceRunningThisTask , final ExecutorService cancellationExecutorService )
        {
            this.exiration = Objects.requireNonNull( expiration );
            this.scheduledExecutorServiceRunningThisTask = Objects.requireNonNull( scheduledExecutorServiceRunningThisTask );
            this.cancellationExecutorService = Objects.requireNonNull( cancellationExecutorService );
            this.whenCreated = Instant.now();
        }

        @Override
        public void run ( )
        {
            Duration elapsed = Duration.between( this.whenCreated , Instant.now() );
            System.out.print( "Core task running. " + Instant.now() + " | Elapsed: " + elapsed + " | " );
            if ( elapsed.toSeconds() > this.exiration.toSeconds() )
            {
                System.out.println( "Core task is asking for cancellation. " + Instant.now() );
                this.cancellationExecutorService.submit( ( ) -> this.scheduledExecutorServiceRunningThisTask.shutdown() );
            } else
            {
                System.out.println( "Core task is completing another `run` execution. " + Instant.now() );
            }
        }
    }
}*/
