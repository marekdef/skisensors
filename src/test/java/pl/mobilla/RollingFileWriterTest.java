package pl.mobilla;

import static org.mockito.Mockito.when;
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
	public void testRollingFileWriter() throws Exception {
		File fileMock = Mockito.mock(File.class);
		when(fileMock.length()).thenReturn(10L);
		when(fileMock.exists()).thenReturn(true);
		
		FileWriter fileWriterMock = Mockito.mock(FileWriter.class);
		
		whenNew(File.class).withAnyArguments().thenReturn(fileMock);
		whenNew(FileWriter.class).withAnyArguments().thenReturn(fileWriterMock);
	}

}
