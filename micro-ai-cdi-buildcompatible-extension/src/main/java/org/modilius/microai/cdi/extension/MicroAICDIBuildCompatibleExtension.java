package org.modilius.microai.cdi.extension;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MicroAICDIBuildCompatibleExtension  implements BuildCompatibleExtension {

    @Discovery
    public void discovery(ScannedClasses scannedClasses, MetaAnnotations metaAnnotations) {
        log.info("===> Call @Discovery");
        //scannedClasses.add(IgniteInstanceProducer.class.getName());
    }

}