package com.olanola.cf.recomender.beans;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import com.olanola.cf.recomender.cdi.MahoutBean;
import org.apache.mahout.cf.taste.common.TasteException;
import org.jboss.ejb3.annotation.TransactionTimeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@LocalBean
@Singleton
@Startup
@TransactionTimeout(value = 3600, unit = TimeUnit.SECONDS)
public class AutostartBean {
    
    @Inject
    private MahoutBean mahoutBean;
    
    @PostConstruct
    public void init() {
        try {
            mahoutBean.initModel();
        } catch (IOException | TasteException e) {
            e.printStackTrace();
        }
    }
    
}
