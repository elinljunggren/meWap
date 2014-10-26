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
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * A class to test event persistence
 * @author Group 1:
 *         Emma Gustafsson
 *         Josefin Ondrus
 *         Elin Ljunggren
 *         Oskar Nyberg
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

    @Before
    @After
    public void beforeAndAfter() throws Exception {
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
        List<Long> dates = new ArrayList<>();
        dates.add(1413278890L);
        dates.add(1413078898L);
        dates.add(1413073898L);
        
        List<MWUser> participators = getDummyUsers();
        
        MWEvent event;
        if (id == -1) {
            event = new MWEvent(name, participators.get(0),"hej", dates, false, 14400, 1413978991L, true, MWEvent.AnswerNotification.EACH_ANSWER, participators);
        } else {
            event = new MWEvent(id, name, participators.get(0), "hej", dates, false, 14400, 1413978991L, true, MWEvent.AnswerNotification.EACH_ANSWER, participators);
        }
        return event;
    }
    
    private List<MWUser> getDummyUsers() {
        List<MWUser> participators = new ArrayList<>();
        MWUser participator = mewap.getUserList().find("asd@asd.asd");
        if (participator == null) {
            participator = new MWUser("asd@asd.asd", "ASD");
            mewap.getUserList().create(participator);
        }
        participators.add(participator);
        participator = mewap.getUserList().find("qwe@qwe.qwe");
        if (participator == null) {
            participator = new MWUser("qwe@qwe.qwe", "QWE");
            mewap.getUserList().create(participator);
        }
        participators.add(participator);
        
        return participators;
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
        List<Long> dates = new ArrayList<>();
        dates.add(1413278890L);
        dates.add(1413078898L);
        dates.add(1413073898L);
        
        List<MWUser> participators = getDummyUsers();
        
        
        MWEvent event = new MWEvent(1L, "Fest", participators.get(0), "hest", dates, false, 14400, 1413978991L, true, MWEvent.AnswerNotification.EACH_ANSWER, participators);
        mewap.getEventList().create(event);
        event = mewap.getEventList().find(1L);
        event.setName("Hest");
        event.setCreator(participators.get(0));
        event.setDescription("fest");  
        event.setDates(dates);
        event.setAllDayEvent(false);
        event.setDuration(14400);
        event.setDeadline(1413978991L);
        event.setDeadlineReminder(true);
        event.setNotification(MWEvent.AnswerNotification.EACH_ANSWER);
        event.setParticipators(participators);
       
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
        List<Long> dates = new ArrayList<>();
        dates.add(1413278890L);
        dates.add(1413078898L);
        event.addAnswer(mewap.getUserList().find("asd@asd.asd"), dates);
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
        em.createQuery("delete from MWUser").executeUpdate();
        utx.commit();
    }

}
