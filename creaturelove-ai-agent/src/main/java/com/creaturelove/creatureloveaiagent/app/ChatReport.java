package com.creaturelove.creatureloveaiagent.app;

import java.util.List;

public record ChatReport(
        String title,
        List<String> suggestions
) { }