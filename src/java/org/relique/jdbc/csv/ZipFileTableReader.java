/*
 *  CsvJdbc - a JDBC driver for CSV files
 *  Copyright (C) 2001  Jonathan Ackerman
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.relique.jdbc.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.relique.io.TableReader;

/**
 * Enables reading CSV files packed in a ZIP file as database tables.
 */
public class ZipFileTableReader implements TableReader
{
	private String zipFilename;
	private ZipFile zipFile;
	private String fileExtension;

	public ZipFileTableReader(String zipFilename) throws IOException {
		this.zipFilename = zipFilename;
		this.zipFile = new ZipFile(zipFilename);
	}

	public void setExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getZipFilename() {
		return zipFilename;
	}

	public Reader getReader(Statement statement, String tableName) throws SQLException {
		try {
			ZipEntry zipEntry = zipFile.getEntry(tableName + fileExtension);
			if (zipEntry == null)
				throw new SQLException("Table not found: " + tableName);
			return new InputStreamReader(zipFile.getInputStream(zipEntry));
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

	public List getTableNames(Connection connection) throws SQLException {
		Vector v = new Vector();
		Enumeration en = zipFile.entries();
		while (en.hasMoreElements()) {
			
			/*
			 * Strip file extensions.
			 */
			String name = ((ZipEntry)en.nextElement()).getName();
			if (name.endsWith(fileExtension))
				name = name.substring(0, name.length() - fileExtension.length());
			v.add(name);
		}
		return v;
	}
}