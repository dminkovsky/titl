/*
 *  titl - Tools for iTunes Libraries
 *  Copyright (C) 2008-2011 Joseph Walton
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.kafsemo.titl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.ZipException;

import org.junit.Test;

public class TestHdfm
{
    private static final byte[] hdfm6Header = {
        0x68, 0x64, 0x66, 0x6d,
        0x00, 0x00, 0x00, (byte) 0x84,
        0x00, 0x68, 0x65, 0x7c,
        0x00, 0x08, 0x00, 0x03,
        0x05, 0x36, 0x2e, 0x30,
        0x2e, 0x34, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x03,
        (byte) 0xb9, 0x18, 0x5c, (byte) 0xfd,
        (byte) 0xbd, (byte) 0xbc, 0x46, 0x06,
        0x00, 0x00, 0x00, 0x09,
        0x02, 0x01, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00
    };
    
    private static byte[] hdfm8Header = {
        0x68, 0x64, 0x66, 0x6d,
        0x00, 0x00, 0x00, (byte) 0x84,
        0x00, 0x00, (byte) 0xed, 0x1b,
        0x00, 0x0e, 0x00, 0x01,
        0x03, 0x38, 0x2e, 0x30,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x06,
        (byte) 0xef, (byte) 0x96, 0x71, (byte) 0xac,
        0x74, 0x72, (byte) 0xd9, 0x7b,
        0x00, 0x00, 0x00, 0x22,
        0x02, 0x01, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x10,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x01, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
    };
    
    @Test
    public void testParse6HdfmHeader() throws Exception
    {
        byte[] wholeFile = new byte[6841724];
        System.arraycopy(hdfm6Header, 0, wholeFile, 0, hdfm6Header.length);
        
        Input di = new InputImpl(new ByteArrayInputStream(wholeFile));
        
        Hdfm hdfm = Hdfm.read(di, wholeFile.length);
        assertNotNull(hdfm);
        assertEquals("6.0.4", hdfm.version);
        assertEquals(wholeFile.length - hdfm6Header.length, hdfm.fileData.length);
    }
    
    @Test
    public void testParse8HdfmHeader() throws Exception
    {
        byte[] wholeFile = new byte[60699];
        System.arraycopy(hdfm8Header, 0, wholeFile, 0, hdfm8Header.length);
        
        Input di = new InputImpl(new ByteArrayInputStream(wholeFile));
        
        Hdfm hdfm = Hdfm.read(di, wholeFile.length);
        assertNotNull(hdfm);
        assertEquals("8.0", hdfm.version);
        assertEquals(wholeFile.length - hdfm8Header.length, hdfm.fileData.length);
    }
    
    @Test
    public void uncompressedDataIsNotInflated() throws Exception
    {
        byte[] data = "Uncompressed.".getBytes("us-ascii");
     
        assertEquals("The same array is returned when data is not compressed",
                data, Hdfm.inflate(data));
    }
    
    @Test
    public void compressedDataIsInflated() throws Exception
    {
        byte[] data = "Uncompressed.".getBytes("us-ascii");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(data);
        out.close();

        byte[] compressed = baos.toByteArray();

        assertFalse(Arrays.equals(data, compressed));
        
        byte[] after = Hdfm.inflate(compressed);
        
        assertArrayEquals("Data is decompressed", data, after);
    }
    
    @Test(expected = ZipException.class)
    public void probablyCompressedFileThrowsExceptionIfErrorDuringDecompression() throws Exception
    {
        byte[] data = "Uncompressed.".getBytes("us-ascii");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(data);
        out.close();

        byte[] compressed = baos.toByteArray();

        /* Intentionally corrupt the compressed data */
        compressed[compressed.length - 1] ^= 0xFF;
        Hdfm.inflate(compressed);
    }
}
