package sure.sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferId;
    private int sourceId;
    private String filepath;

    private boolean isPlaying;

    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;

        // allocate memory to store the return information from stb
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudio = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);
        if (rawAudio == null) {
            System.out.println("There was a problem while decoding " + filepath);
            stackPop();
            stackPop();
            return;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        stackPop();
        stackPop();

        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        this.bufferId = alGenBuffers();
        alBufferData(bufferId, format, rawAudio, sampleRate);

        sourceId = alGenSources();

        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
        alSourcei(sourceId, AL_POSITION, 0);
        alSourcef(sourceId, AL_GAIN, 0.3f);

        free(rawAudio);
    }

    public void delete() {
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }

    public void play() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);
        }

        if (!isPlaying) {
            alSourcePlay(sourceId);
            isPlaying = true;
        }
    }

    public void stop() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_PLAYING)
            isPlaying = true;

        if (isPlaying) {
            alSourceStop(sourceId);
            isPlaying = false;
        }
    }

    public void restart() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        alSourcei(sourceId, AL_POSITION, 0);

        if (state == AL_STOPPED) {
            isPlaying = false;
        }

        if (!isPlaying) {
            alSourcePlay(sourceId);
            isPlaying = true;
        }
    }
}
