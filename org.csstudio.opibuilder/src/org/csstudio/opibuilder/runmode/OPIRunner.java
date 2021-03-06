/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.runmode;

import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.csstudio.opibuilder.actions.CompactModeAction;
import org.csstudio.opibuilder.model.DisplayModel;
import org.csstudio.opibuilder.util.WorkbenchWindowService;
import org.csstudio.ui.util.NoResourceEditorInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * The editor for running of OPI.
 * 
 * @author Xihui Chen
 * 
 */
public class OPIRunner extends EditorPart implements IOPIRuntime{


	public static final String ID = "org.csstudio.opibuilder.OPIRunner"; //$NON-NLS-1$
	

	private OPIRuntimeDelegate opiRuntimeDelegate;

	public OPIRunner() {
		opiRuntimeDelegate = new OPIRuntimeDelegate(this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input)
			throws PartInitException {
		setSite(site);
		
		setInput(!(input instanceof IRunnerInput) && !(input instanceof NoResourceEditorInput) ? 
						new NoResourceEditorInput(input) : input);
		opiRuntimeDelegate.init(site, input instanceof NoResourceEditorInput ? 
				((NoResourceEditorInput)input).getOriginEditorInput() : input);
	}
	
	public void setOPIInput(IEditorInput input) throws PartInitException {
		init(getEditorSite(), input);
	}


	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(final Composite parent) {
		opiRuntimeDelegate.createGUI(parent);
		//if this is the first OPI in this window, resize the window to match the OPI size.
		//Make it in compact mode if it is configured.
		if(OPIBuilderPlugin.isRAP())
			return;
		Display.getCurrent().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if (getSite().getWorkbenchWindow().getActivePage()
						.getEditorReferences().length == 1 && 
						getSite().getWorkbenchWindow().getActivePage().getViewReferences().length ==0) {
					int trimWidth = 45, trimHeight = 165;
					CompactModeAction action = WorkbenchWindowService.getInstance().getCompactModeAction(
							getSite().getWorkbenchWindow());
					if (WorkbenchWindowService.isInCompactMode()) {
						if(!action.isInCompactMode())
							action.run();
						trimHeight = 65;
					}
					final Rectangle bounds;
					if (opiRuntimeDelegate.getDisplayModel() != null)
						bounds = opiRuntimeDelegate.getDisplayModel()
								.getBounds();
					else
						bounds = new Rectangle(-1, -1, 800, 600);
					if (bounds.x >= 0 && bounds.y >= 0)
						parent.getShell().setLocation(bounds.x, bounds.y);
					parent.getShell().setSize(bounds.width + trimWidth,
							bounds.height + trimHeight);
				}
			}
		});
		
	}

	@Override
	public void setFocus() {}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		Object obj = opiRuntimeDelegate.getAdapter(adapter);
		if(obj != null)
			return obj;
		else
			return super.getAdapter(adapter);

	}

	public void setWorkbenchPartName(String name) {
		setPartName(name);
	}

	public OPIRuntimeDelegate getOPIRuntimeDelegate() {
		return opiRuntimeDelegate;
	}
	
	public IEditorInput getOPIInput() {
		return getOPIRuntimeDelegate().getEditorInput();
	}
	
	public DisplayModel getDisplayModel() {
		return getOPIRuntimeDelegate().getDisplayModel();
	}
}
