package com.denbestegrupp.mewap.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing the persistence layer
 *
 * NOTE NOTE NOTE: JavaDB (Derby) must be running (not using an embedded
 * database) GlassFish not needed using embedded
 *
 * @author hajo
 */
@RunWith(Arquillian.class)
public class EventPersistenceTest {

    @Inject
    MeWap mewap;
    
    @Inject
    UserTransaction utx;  // This is not an EJB so have to handle transactions

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                // Add all classes
                .addPackage("com.denbestegrupp.mewap.model")
                // This will add test-persitence.xml as persistence.xml (renamed)
                // in folder META-INF, see Files > jpa_managing > target > arquillian
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                // Must have for CDI to work
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Before  // Run before each test
    public void before() throws Exception {
        clearAll();
    }

    @Test
    public void TestPersistAndFindAnEvent() throws Exception {
        List<Date> dates = new ArrayList<>();
        dates.add(new Date(1413278890));
        dates.add(new Date(1413078898));
        dates.add(new Date(1413073898));
        MWEvent event = new MWEvent(1L, "Fest", dates, 14400, new Date(1413978991), true, MWEvent.AnswerNotification.EACH_ANSWER);
        mewap.getEventList().create(event);
        MWEvent dbevent = mewap.getEventList().find(1L);
        assertTrue(dbevent.equals(event));
    }
    
    @Test
    public void TestUpdateAndGetEventByName() throws Exception {
        List<Date> dates = new ArrayList<>();
        dates.add(new Date(1413278890));
        dates.add(new Date(1413078898));
        dates.add(new Date(1413073898));
        MWEvent event = new MWEvent(1L, "Fest", dates, 14400, new Date(1413978991), true, MWEvent.AnswerNotification.EACH_ANSWER);
        mewap.getEventList().create(event);
        event = new MWEvent(1L, "Hest", dates, 14400, new Date(1413978991), true, MWEvent.AnswerNotification.EACH_ANSWER);
        mewap.getEventList().update(event);
        MWEvent dbevent = mewap.getEventList().getByName("Hest").get(0);
        assertTrue(dbevent.equals(event));
    }

    // Need a standalone em to remove testdata between tests
    // No em accessible from interfaces
    @PersistenceContext(unitName = "mewap_test_pu")
    @Produces
    @Default
    EntityManager em;

    // Order matters
    private void clearAll() throws Exception {  
        utx.begin();  
        em.joinTransaction();
        em.createQuery("delete from MWEvent").executeUpdate();
        em.createQuery("delete from MWAnswer").executeUpdate();
        utx.commit();
    }

}
