package org.yerbabuena.athento.cad.viewer.ejb;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

@Name("cadViewerBean")
@Scope(ScopeType.PAGE)
public class CadViewerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(CadViewerBean.class);

	private static String AUTOCAD_DWG_MIMETYPE = "image/vnd.dwg";
	
	private static String APACHE_PATH = Framework.getProperty("athento.cad.viewer.apache.path", "/var/www/imagenes/");
	
	private static String NUXEO_HOST = Framework.getProperty("athento.cad.viewer.host", "http://localhost:8080/nuxeo");
	
	private static String CAD_FILES_SERVER = Framework.getProperty("athento.cad.files.server", "http://localhost/imagenes");
	
	private static String TMP_DIRECTORY = Framework.getProperty("athento.cad.viewer.tmp.directory", "/tmp");
	
	private static String APP = Framework.getProperty("athento.cad.viewer.app", "gedit");
	
	@Create
	public void initialize() {
		
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public boolean isCadValidFile(DocumentModel doc) {
		
		boolean isCadValidFile = false;
		
		try {
			if (doc.getProperty("file", "content") != null) {
				Blob blob = (Blob) doc.getProperty("file", "content");	
				
				String filename = blob.getFilename();
				String mimetype = blob.getMimeType();
				
				LOG.info("Mimetype: " + mimetype);
				
				if (mimetype.equals(AUTOCAD_DWG_MIMETYPE) || filename.contains(".dwg")) {
					isCadValidFile = true;
				}
			}
		} catch (Exception e) {
			LOG.error("ERROR: ", e);
		}
		
		return isCadValidFile;
	}
	
	public void copyFileOnPath(DocumentModel doc) {
		
		try {
			if (doc.getProperty("file", "content") != null) {
				Blob blob = (Blob) doc.getProperty("file", "content");	
				
				// Copy file on apache server
				File tempfile = new File(APACHE_PATH + doc.getId() + ".dwg");
				blob.transferTo(tempfile);
			}
		} catch (Exception e) {
			LOG.error("ERROR: ", e);
		}
		
	}
	
	public String getHost() {
		return NUXEO_HOST;
	}
	
	public String getTmpDirectory() {
		return TMP_DIRECTORY;
	}
	
	public String getApp() {
		return APP;
	}
	
	public String getCadFilesServer() {
		return CAD_FILES_SERVER;
	}
}
