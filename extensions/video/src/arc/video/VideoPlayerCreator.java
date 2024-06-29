/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package arc.video;

import arc.Application.ApplicationType;
import arc.Core;
import arc.util.ArcRuntimeException;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Reflect;

/** This class is used to provide a way of creating a VideoPlayer, without knowing the platform the program is running on. This
 * has to be extended for each supported platform.
 *
 * @author Rob Bogie rob.bogie@codepoke.net */
public class VideoPlayerCreator {
	private static Class<? extends VideoPlayer> videoPlayerClass;

	/** Creates a VideoPlayer.
	 *
	 * @return A new instance of VideoPlayer */
	@Nullable
	public static VideoPlayer createVideoPlayer () {
		initialize();
		if (videoPlayerClass == null) return new VideoPlayerStub();
		try {
			return Reflect.cons(videoPlayerClass).get();
		} catch (ArcRuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static void initialize () {
		if (videoPlayerClass != null) return;

		String className = null;
		ApplicationType type = Core.app.getType();

		if (type == ApplicationType.android) {
			if (Core.app.getVersion() >= 12) {
				className = "com.badlogic.gdx.video.VideoPlayerAndroid";
			} else {
				Log.infoTag("Core-Video", "VideoPlayer can't be used on android < API level 12");
			}
		} else if (type == ApplicationType.iOS) {
			if (Core.app.getVersion() >= 15) {
				className = "com.badlogic.gdx.video.VideoPlayerIos";
			} else {
				Log.infoTag("Core-Video", "VideoPlayer can't be used on iOS < 15");
			}
		} else if (type == ApplicationType.desktop) {
			className = "com.badlogic.gdx.video.VideoPlayerDesktop";
		} else if (type == ApplicationType.web) {
			className = "com.badlogic.gdx.video.VideoPlayerGwt";
		} else {
			Log.infoTag("Core-Video", "Platform is not supported by the Core Video Extension");
		}

		try {
			videoPlayerClass = Reflect.make(className);
		} catch (ArcRuntimeException e) {
			e.printStackTrace();
		}
	}
}
