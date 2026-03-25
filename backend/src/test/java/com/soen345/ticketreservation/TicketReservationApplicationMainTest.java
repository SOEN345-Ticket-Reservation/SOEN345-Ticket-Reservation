package com.soen345.ticketreservation;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.times;

class TicketReservationApplicationMainTest {

    @Test
    void main_DelegatesToSpringApplicationRun() {
        String[] args = { "--spring.profiles.active=test" };

        try (MockedStatic<SpringApplication> springApplicationMock = org.mockito.Mockito
                .mockStatic(SpringApplication.class)) {
            TicketReservationApplication.main(args);

            springApplicationMock.verify(
                    () -> SpringApplication.run(TicketReservationApplication.class, args),
                    times(1));
        }
    }
}
