/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.propsheet;

import org.csstudio.swt.xygraph.undo.IUndoableCommand;
import org.csstudio.swt.xygraph.undo.OperationsManager;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.model.ArchiveRescale;
import org.csstudio.trends.databrowser2.model.Model;

/** Undo-able command to change archive rescale behavior
 *  @author Kay Kasemir
 */
public class ChangeArchiveRescaleCommand implements IUndoableCommand
{
    final private Model model;
    final private ArchiveRescale old_rescale, new_rescale;

    /** Register and perform the command
     *  @param item Model item to configure
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param period New value
     */
    public ChangeArchiveRescaleCommand(final Model model,
            final OperationsManager operations_manager,
            final ArchiveRescale rescale)
    {
        this.model = model;
        this.old_rescale = model.getArchiveRescale();
        this.new_rescale = rescale;
        operations_manager.addCommand(this);
        redo();
    }

    /** {@inheritDoc} */
    @Override
    public void redo()
    {
        model.setArchiveRescale(new_rescale);
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        model.setArchiveRescale(old_rescale);
    }

    /** @return Command name that appears in undo/redo menu */
    @Override
    public String toString()
    {
        return Messages.ArchiveRescale_Label;
    }
}
