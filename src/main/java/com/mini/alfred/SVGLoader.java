package com.mini.alfred;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javafx.scene.image.Image;

public class SVGLoader {
    public static Image loadSVGImage(String svgContent) {
        // 使用Data URI格式
        String dataUri = "data:image/svg+xml;charset=UTF-8," + 
                          java.net.URLEncoder.encode(svgContent, StandardCharsets.UTF_8);
        
        return new Image(dataUri);
    }
} 