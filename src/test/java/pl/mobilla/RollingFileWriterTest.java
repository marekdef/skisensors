package pl.mobilla;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.File;
import java.io.FileWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ RollingFileWriter.class })
public class RollingFileWriterTest {

	@Test
	public void testRollingFileWriterWithSmallFile() throws Exception {
		File fileMock = Mockito.mock(File.class);
		when(fileMock.length()).thenReturn(10L);
		when(fileMock.exists()).thenReturn(true);
		
		FileWriter fileWriterMock = Mockito.mock(FileWriter.class);
		
		whenNew(File.class).withAnyArguments().thenReturn(fileMock);
		whenNew(FileWriter.class).withAnyArguments().thenReturn(fileWriterMock);

        RollingFileWriter rfw = new RollingFileWriter("/mnt/sdcard/test.log", 512);

        verifyNew(FileWriter.class).withArguments("/mnt/sdcard/test.log");
	}

    @Test
    public void testRollingFileWriterWithBigFile() throws Exception {
        File fileMock = Mockito.mock(File.class);
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.length()).thenReturn(513L);

        File fileMock2 = Mockito.mock(File.class);
        when(fileMock2.exists()).thenReturn(true);
        when(fileMock2.length()).thenReturn(0L);

        FileWriter fileWriterMock = Mockito.mock(FileWriter.class);

        whenNew(File.class).withArguments("/mnt/sdcard/test.log").thenReturn(fileMock);
        whenNew(File.class).withArguments("/mnt/sdcard/test.1.log").thenReturn(fileMock2);
        whenNew(FileWriter.class).withAnyArguments().thenReturn(fileWriterMock);

        RollingFileWriter rfw = new RollingFileWriter("/mnt/sdcard/test.log", 512);

        verifyNew(FileWriter.class).withArguments("/mnt/sdcard/test.1.log");
    }

    @Test
    public void testRollingFileWriterChangeFile() throws Exception {
        File fileMock = Mockito.mock(File.class);
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.length()).thenReturn(0L);

        File fileMock2 = Mockito.mock(File.class);
        when(fileMock2.exists()).thenReturn(true);
        when(fileMock2.length()).thenReturn(0L);

        FileWriter fileWriterMock = Mockito.mock(FileWriter.class);

        whenNew(File.class).withArguments("/mnt/sdcard/test.log").thenReturn(fileMock);
        whenNew(File.class).withArguments("/mnt/sdcard/test.1.log").thenReturn(fileMock2);
        whenNew(FileWriter.class).withAnyArguments().thenReturn(fileWriterMock);

        RollingFileWriter rfw = new RollingFileWriter("/mnt/sdcard/test.log", 512);
        verifyNew(FileWriter.class).withArguments("/mnt/sdcard/test.log");

        rfw.write("Hello world\n");

        verify(fileWriterMock, atLeastOnce()).write("Hello world\n");
        when(fileMock.length()).thenReturn(513L);

        rfw.write("Hello world\n");
        verifyNew(FileWriter.class).withArguments("/mnt/sdcard/test.1.log");
    }

}
