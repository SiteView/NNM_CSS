/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.widgets.model;

import org.csstudio.opibuilder.properties.BooleanProperty;
import org.csstudio.opibuilder.properties.StringProperty;
import org.csstudio.opibuilder.properties.WidgetPropertyCategory;
import org.csstudio.opibuilder.util.WidgetDescriptor;
import org.csstudio.opibuilder.util.WidgetsService;


/**
 * The widget model for LED.
 * @author Xihui Chen
 *
 */
public class LEDModel extends AbstractBoolWidgetModel {

	/**
	 * The lampID of the widget lamp.
	 */
	public static final String PROP_LAMPID = "lampID";//$NON-NLS-1$
	
	/** The ID of the effect 3D property. */
	public static final String PROP_EFFECT3D = "effect_3d"; //$NON-NLS-1$
	
	/** The ID of the square LED property. */
	public static final String PROP_SQUARE_LED = "square_led"; //$NON-NLS-1$
	
	
	/** The default value of the height property. */	
	private static final int DEFAULT_HEIGHT = 20;
	
	/** The default value of the width property. */
	private static final int DEFAULT_WIDTH = 20;
	
	public static final int MINIMUM_SIZE = 10;

	
	public LEDModel() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setScaleOptions(true, true, true);
	}
	
	@Override
	protected void configureProperties() {
		super.configureProperties();
		WidgetDescriptor descriptor = WidgetsService.getInstance().getWidgetDescriptor(getTypeID());
		String lampID;
		lampID = descriptor == null? getTypeID().substring(getTypeID().lastIndexOf(".")+1) :
			descriptor.getName();
		addProperty(new StringProperty(PROP_LAMPID, "lampID", 
				WidgetPropertyCategory.Basic, lampID));
		addProperty(new BooleanProperty(PROP_EFFECT3D, "3D Effect", 
				WidgetPropertyCategory.Display, true));
		
		addProperty(new BooleanProperty(PROP_SQUARE_LED, "Square LED", 
				WidgetPropertyCategory.Display, false));
		setPropertyVisible(PROP_BOOL_LABEL_POS, false);
	}
	/**
	 * The ID of this widget model.
	 */
	public static final String ID = "org.csstudio.opibuilder.widgets.LED"; //$NON-NLS-1$	
	
	@Override
	public String getTypeID() {
		return ID;
	}

	/**
	 * @return true if the widget would be painted with 3D effect, false otherwise
	 */
	public boolean isEffect3D() {
		return (Boolean) getProperty(PROP_EFFECT3D).getPropertyValue();
	}
	
	/**
	 * @return true if the LED is square, false otherwise
	 */
	public boolean isSquareLED() {
		return (Boolean) getProperty(PROP_SQUARE_LED).getPropertyValue();
	}
	
	public String getLampID(){
		return (String)getCastedPropertyValue(PROP_LAMPID);
	}
	
	public void setLampID(String lampID){
		setPropertyValue(PROP_LAMPID, lampID);
	}
}
