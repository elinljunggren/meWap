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
//@RunWith(Arquillian.class)
public class EventPersistenceTest {

    /*@Inject
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
    
    private MWEvent createDummyEvent() {
        return createDummyEvent(-1, "fest");
    }
    
    private MWEvent createDummyEvent(long id) {
        return createDummyEvent(id, "fest");
    }
    
    private MWEvent createDummyEvent(String name) {
        return createDummyEvent(-1, name);
    }

    private MWEvent createDummyEvent(long id, String name) {
        List<Date> dates = new ArrayList<>();
        dates.add(new Date(1413278890));
        dates.add(new Date(1413078898));
        dates.add(new Date(1413073898));
        MWEvent event;
        if (id == -1) {
            event = new MWEvent(name, dates, 14400, new Date(1413978991), true, MWEvent.AnswerNotification.EACH_ANSWER);
        } else {
            event = new MWEvent(id, name, dates, 14400, new Date(1413978991), true, MWEvent.AnswerNotification.EACH_ANSWER);
        }
        return event;
    }
    
    @Test
    public void TestPersistAndFindAnEvent() throws Exception {
        MWEvent event = createDummyEvent(1L);
        mewap.getEventList().create(event);
        MWEvent dbevent = mewap.getEventList().find(1L);
        assertTrue(dbevent.equals(event));
    }
    
    @Test
    public void TestGetByName() throws Exception {
        List<MWEvent> events = new ArrayList<>();
        for (int i=0; i<5; i++) {
            MWEvent event = createDummyEvent("hest");
            events.add(event);
            mewap.getEventList().create(event);
        }
        List<MWEvent> dbevents = mewap.getEventList().getByName("hest");
        assertTrue(dbevents.equals(events));
    }
    
    @Test
    public void TestUpdateEvent() throws Exception {
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
    
    @Test
    public void TestDeleteEvent() throws Exception {
        MWEvent event = createDummyEvent(1L);
        mewap.getEventList().create(event);
        mewap.getEventList().delete(1L);
        MWEvent dbevent = mewap.getEventList().find(1L);
        assertTrue(dbevent == null);
    }
    
    @Test
    public void TestCount() throws Exception {
        MWEvent event = createDummyEvent(1L);
        mewap.getEventList().create(event);
        assertTrue(mewap.getEventList().count() == 1);
        event = createDummyEvent(2L);
        mewap.getEventList().create(event);
        assertTrue(mewap.getEventList().count() == 2);
        mewap.getEventList().delete(2L);
        assertTrue(mewap.getEventList().count() == 1);
    }
    
    @Test
    public void TestFindAll() throws Exception {
        List<MWEvent> events = new ArrayList<>();
        for (int i=0; i<5; i++) {
            MWEvent event = createDummyEvent("hest" + i);
            events.add(event);
            mewap.getEventList().create(event);
        }
        List<MWEvent> dbevents = mewap.getEventList().findAll();
        assertTrue(dbevents.equals(events));
    }
    
    @Test
    public void TestFindRange() throws Exception {
        List<MWEvent> events = new ArrayList<>();
        for (int i=0; i<10; i++) {
            MWEvent event = createDummyEvent("hest" + i);
            mewap.getEventList().create(event);
            if (i >= 3 && i < 7) {
                events.add(event);
            }
        }
        List<MWEvent> dbevents = mewap.getEventList().findRange(3, 4);
        assertTrue(dbevents.equals(events));
    }
    
    @Test
    public void TestAddAnswer() throws Exception {
        MWEvent event = createDummyEvent(1L);
        mewap.getEventList().create(event);
        List<Date> dates = new ArrayList<>();
        dates.add(new Date(1413278890));
        dates.add(new Date(1413078898));
        event.addAnswer(new MWUser("email@email.email", "name name"), dates);
        mewap.getEventList().update(event);
        dates.add(new Date(1413278891));
        dates.add(new Date(1413078899));
        event.addAnswer(new MWUser("email2@email2.email2", "name2 name2"), dates);
        mewap.getEventList().update(event);
        
        List<MWAnswer> answers = mewap.getEventList().find(1L).getAnswers();
        assertTrue(answers.equals(event.getAnswers()));
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
*/
}
