package org.csstudio.trends.databrowser2.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;

import org.csstudio.trends.databrowser2.persistence.URLPath;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.PlatformUI;

/**Utility functions for resources.
 * @author Xihui Chen, Abadie Lana, Kay Kasemir
 */
public class ResourceUtil {

	private static final ResourceUtilSSHelper IMPL;
	
	static {
		IMPL = (ResourceUtilSSHelper)ImplementationLoader.newInstance(
				ResourceUtilSSHelper.class);
	}
	
	
		
	/**
	 *Return the {@link InputStream} of the file that is available on the
	 * specified path. The task will run in UIJob. Caller must be in UI thread too.
	 * @see #pathToInputStream(IPath, boolean)
	 */
	public static InputStream pathToInputStream(final IPath path) throws Exception {
		return pathToInputStream(path, true);
	}
	/**
	 * Return the {@link InputStream} of the file that is available on the
	 * specified path.
	 *
	 * @param path
	 *            The {@link IPath} to the file in the workspace, the local
	 *            file system, or a URL (http:, https:, ftp:, file:, platform:)
	 * @param runInUIJob
	 * 				true if the task should run in UIJob, which will block UI responsiveness with a progress bar
	 * on status line. Caller must be in UI thread if this is true.
	 * @return The corresponding {@link InputStream}. Never <code>null</code>
	 * @throws Exception
	 */
	@SuppressWarnings("nls")
    public static InputStream pathToInputStream(final IPath path, boolean runInUIJob) throws Exception
    {
	   return IMPL.pathToInputStream(path, runInUIJob);
	}
	
	/**
	 * @param path the file path
	 * @return true if the file path is an existing workspace file.
	 */
	public static boolean isExistingWorkspaceFile(IPath path){
		return IMPL.isExistingWorkspaceFile(path);
	}
	
	public static boolean isExistingLocalFile(IPath path){
		 // Not a workspace file. Try local file system
        File local_file = path.toFile();
       
        // Path URL for "file:..." so that it opens as FileInputStream
        if (local_file.getPath().startsWith("file:"))
            local_file = new File(local_file.getPath().substring(5));
        return local_file.exists();
	}

	/**Build the relative path from a reference path.
	 * @param refPath the reference path which does not include the file name.
	 * @param fullPath the absolute full path which includes the file name.
	 * @return the relative to path to refPath.
	 */
	public static IPath buildRelativePath(IPath refPath, IPath fullPath){
		if(refPath == null || fullPath == null)
			throw new NullPointerException();
		return fullPath.makeRelativeTo(refPath);
	}

	/**Returns IPath from String.
	 * @param input the path string.
	 * @return {@link URLPath} if input is an URL; returns {@link Path} otherwise.
	 */
	public static IPath getPathFromString(String input){
		if(input == null)
			return null;
		if(isURL(input))
			return new URLPath(input);
		else
			return new Path(input);
	}

	/** Check if a URL is actually a URL
	 *  @param url Possible URL
	 *  @return <code>true</code> if considered a URL
	 */
	@SuppressWarnings("nls")
    public static boolean isURL(final String urlString){
		try {
			new URL(urlString);
		} catch (Exception e) {
			return false;
		}
		return true;
//		return urlString.contains(":/");  //$NON-NLS-1$
	}
	
	private static InputStream inputStream;
	private static Object lock = new Boolean(true);
	
	/**Open URL stream in UI Job if runInUIJob is true.
	 * @param url
	 * @param runInUIJob true if this method should run as an UI Job. 
	 * If it is true, this method must be called in UI thread.
	 * @return
	 * @throws Exception
	 */
	public static InputStream openURLStream(final URL url, boolean runInUIJob) throws Exception {
		inputStream = null;
		if (runInUIJob) {
			synchronized (lock) {
				IRunnableWithProgress openURLTask = new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						try {
							monitor.beginTask("Connecting to " + url,
									IProgressMonitor.UNKNOWN);
							inputStream = openURLStream(url);
						} catch (IOException e) {
							throw new InvocationTargetException(e,
									"Timeout while connecting to " + url);
						} finally {
							monitor.done();
						}
					}

				};
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.run(true, false, openURLTask);
			}
		}else
			return openURLStream(url);
		return inputStream;
	}
	
	/**Open URL Stream.
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static InputStream openURLStream(final URL url) throws IOException{
		URLConnection connection = url.openConnection();
//		connection.setReadTimeout(Preferences.getURLFileLoadingTimeout());
		return connection.getInputStream();
	}
	
	/**If the path is an connectable URL. .
	 * @param path
	 * @param runInUIJob true if this method should run as an UI Job. 
	 * If it is true, this method must be called in UI thread.
	 * @return
	 */
	public static boolean isExistingURL(final IPath path, boolean runInUIJob){
		try {
			InputStream s = openURLStream(new URL(path.toString()), runInUIJob);
			if(s != null){
				s.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**If the file on path is an existing file in workspace, local file system or available URL.
	 * @param absolutePath
	 * @param runInUIJob true if this method should run as an UI Job. 
	 * If it is true, this method must be called in UI thread.
	 * @return
	 */
	public static boolean isExsitingFile(final IPath absolutePath, boolean runInUIJob){
		if(isExistingWorkspaceFile(absolutePath))
			return true;
		if(isExistingLocalFile(absolutePath))
			return true;
		if(isExistingURL(absolutePath, runInUIJob))
			return true;
		return false;
	}
	
	/**Get screenshot image from GraphicalViewer
	 * @param viewer the GraphicalViewer
	 * @return the screenshot image
	 */
	public static Image getScreenshotImage(GraphicalViewer viewer){		
		return IMPL.getScreenShotImage(viewer);
	}
	
	@SuppressWarnings("nls")
    public static String getScreenshotFile(GraphicalViewer viewer) throws Exception{
		File file;
		 // Get name for snapshot file
        try
        {
            file = File.createTempFile("databrowser", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
            file.deleteOnExit();
        }
        catch (Exception ex)
        {
            throw new Exception("Cannot create tmp. file:\n" + ex.getMessage());
        }

        // Create snapshot file
        try
        {
            final ImageLoader loader = new ImageLoader();

            final Image image = ResourceUtil.getScreenshotImage(viewer);
            loader.data = new ImageData[]{image.getImageData()};
            image.dispose();
            loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
        }
        catch (Exception ex)
        {
            throw new Exception(
                    NLS.bind("Cannot create snapshot in {0}:\n{1}",
                            file.getAbsolutePath(), ex.getMessage()));
        }
        return file.getAbsolutePath();
    }

}
