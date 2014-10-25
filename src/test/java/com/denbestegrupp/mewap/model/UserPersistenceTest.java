package com.denbestegrupp.mewap.model;

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

@RunWith(Arquillian.class)
public class UserPersistenceTest {

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
    public void before() throws Exception {
        clearAll();
    }
    
    @After
    public void after() throws Exception {
        clearAll();
    }
    
    @Test
    public void testPersistAnUserAndfind() {
        MWUser user = new MWUser("a@a.a", "a");
        mewap.getUserList().create(user);
        assertTrue(mewap.getUserList().find("a@a.a").equals(user));
    }
    
    @Test
    public void testGetByName() {
        MWUser user = new MWUser("a@a.a", "a");
        mewap.getUserList().create(user);
        assertTrue(mewap.getUserList().getByName("a").get(0).equals(user));
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
