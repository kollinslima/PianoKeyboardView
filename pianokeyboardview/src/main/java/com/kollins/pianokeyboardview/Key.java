/*
 * Copyright 2019
 * Kollins Lima (kollins.lima@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kollins.pianokeyboardview;

import android.graphics.RectF;

public class Key {

    public RectF keyDesign;
    public boolean down;
    public int midiNote;

    public Key(RectF keyDesign, int midiNote) {
        this.keyDesign = keyDesign;
        this.down = false;
        this.midiNote = midiNote;
    }
}
