/*******************************************************************************
* Copyright (c) 2010-2012 ITER Organization.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
******************************************************************************/
package org.csstudio.alarm.beast.notifier;

/**
 * Details about an automated action.
 * @author Fred Arnaud (Sopra Group)
 *
 */
public class AAData {

	/**
	 * The details text under the title. You must use empty string ("") not null
	 * if there is no details under the title.
	 */
	final private String details;

	/** The delay for the action. */
	final private int delay;

	public AAData(final String details, final int delay) {
		this.details = details;
		this.delay = delay;
	}

	public String getDetails() {
		return details;
	}
	public int getDelay() {
		return delay;
	}

}
