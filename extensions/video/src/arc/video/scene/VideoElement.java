/*******************************************************************************
 * Copyright 2023 See AUTHORS file.
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

package arc.video.scene;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.scene.Element;
import arc.util.ArcRuntimeException;
import arc.util.Reflect;
import arc.video.VideoPlayer;

import static arc.Core.batch;
import static arc.graphics.g2d.Draw.getColor;

/** A simple actor that allows you to integrate a video in a 2D scene.
 * <p>
 * The actor takes care of updating and displaying the video on each frame. The creation, video loading, playback control, and
 * disposing of the {@link VideoPlayer} is left to the user.
 * <p>
 * The actor must be removed before disposing the VideoPlayer and must not be used again after. */
public class VideoElement extends Element {
	/** Creates a new video actor that displays the video from the provided player.
	 *
	 * @param player The video player that this actor uses. Must not be null! */
	public VideoElement(VideoPlayer player) {
		if (player == null) throw new ArcRuntimeException("VideoActor: player must not be null!");
		this.player = player;
	}

	private final VideoPlayer player;

	@Override
	public void act(float delta) {
		super.act(delta);
		player.update();
	}

	@Override
	public void draw() {
		Texture texture = player.getTexture();
		if (texture == null) return;
		Color color = getColor();
		Reflect.invoke(batch, "setColor", new Object[]{color.r, color.g, color.b, color.a * parentAlpha});
		Reflect.invoke(batch, "draw", new Object[]{texture, getX(0), getY(0), getWidth(), getHeight(), player.getVideoWidth(), player.getVideoHeight(), 0});
	}

	public VideoPlayer getVideoPlayer() {
		return player;
	}
}
