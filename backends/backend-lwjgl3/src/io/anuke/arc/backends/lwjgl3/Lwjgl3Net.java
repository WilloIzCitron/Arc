/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package io.anuke.arc.backends.lwjgl3;

import io.anuke.arc.Net;
import io.anuke.arc.function.Consumer;
import io.anuke.arc.util.NetJavaImpl;
import io.anuke.arc.util.OS;

import java.io.*;

/**
 * LWJGL implementation of the {@link Net} API, it could be reused in other Desktop backends since it doesn't depend on LWJGL.
 * @author acoppes
 */
public class Lwjgl3Net implements Net{
    NetJavaImpl impl = new NetJavaImpl();

    @Override
    public void http(HttpRequest httpRequest, Consumer<HttpResponse> success, Consumer<Throwable> failure){
        impl.http(httpRequest, success, failure);
    }

    @Override
    public boolean openURI(String url){
        try{
            if(OS.isMac){
                Class.forName("com.apple.eio.FileManager").getMethod("openURL", String.class).invoke(null, url);
                return true;
            }else if(OS.isLinux){
                exec("xdg-open " + url);
                return true;
            }else if(OS.isWindows){
                exec("rundll32 url.dll,FileProtocolHandler " + url);
                return true;
            }
            return false;
        }catch(Throwable e){
            e.printStackTrace();
            return false;
        }
    }

    private void exec(String command) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

}
