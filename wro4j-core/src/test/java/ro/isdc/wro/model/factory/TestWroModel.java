/*
 * Copyright (c) 2010. All rights reserved.
 */
package ro.isdc.wro.model.factory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.model.WroModel;
import ro.isdc.wro.model.group.Group;
import ro.isdc.wro.model.group.InvalidGroupNameException;

/**
 * Test class for WroModel..
 *
 * @author Alex Objelean
 * @created Created on Jan 6, 2010
 */
public class TestWroModel {
  private WroModel model;
  private WroModelFactory factory;
  @Before
  public void setUp() {
    final Context context = Context.standaloneContext();
    Context.set(context);
    model = buildValidModel();
  }

  @After
  public void tearDown() {
    factory.destroy();
  }

  @Test
  public void testGetExistingGroup() {
    Assert.assertFalse(model.getGroups().isEmpty());
    final Group group = model.getGroupByName("g1");
    //create a copy of original list
    Assert.assertEquals(1, group.getResources().size());
  }

  @Test
  public void testGetGroupNames() {
    final List<String> groupNames = model.getGroupNames();
    Collections.sort(groupNames);
    final List<String> expected = Arrays.asList("g1","g2","g3");
    Assert.assertEquals(expected, groupNames);
  }

  @Test(expected=InvalidGroupNameException.class)
  public void testGetInvalidGroup() {
    Assert.assertFalse(model.getGroups().isEmpty());
    model.getGroupByName("INVALID_GROUP");
  }

  /**
   * @return a valid {@link WroModel} pre populated with some valid resources.
   */
  private WroModel buildValidModel() {
    factory = new XmlModelFactory() {
      @Override
      protected InputStream getModelResourceAsStream() {
        return getClass().getResourceAsStream("wro.xml");
      }
    };
    //the uriLocator factory doesn't have any locators set...
    final WroModel model = factory.create();
    return model;
  }
  
  @Test
  public void shouldHaveNoUsedGroupsByDefault() {
    Assert.assertTrue(model.getUsedGroups().isEmpty());
  }
  
  @Test
  public void shouldReturnCorrectCollectionOfUsedGroups() {
    final WroModel model = new WroModel();
    final Group g1 = new Group("g1");
    final Group g2 = new Group("g2");
    model.addGroup(g1);
    model.addGroup(g2);
    Assert.assertEquals(2, model.getGroups().size());
    Assert.assertEquals(0, model.getUsedGroups().size());
    
    g1.markAsUsed();
    Assert.assertEquals(1, model.getUsedGroups().size());
  }
}
