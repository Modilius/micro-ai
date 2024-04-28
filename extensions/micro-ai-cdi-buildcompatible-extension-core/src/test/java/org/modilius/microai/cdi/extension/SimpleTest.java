package org.modilius.microai.cdi.extension;

// org.modilius.microai

import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.testing.junit5.AddBean;
import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.AddExtension;
import io.helidon.microprofile.testing.junit5.DisableDiscovery;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@HelidonTest
@DisableDiscovery
@AddBean(DummyBean.class)
@AddExtension(ConfigCdiExtension.class)
@AddConfig(key = "app.greeting", value = "TestHello")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SimpleTest {
    private final DummyBean dummyBean;

    @Test
    void dummy() {
    }

}
