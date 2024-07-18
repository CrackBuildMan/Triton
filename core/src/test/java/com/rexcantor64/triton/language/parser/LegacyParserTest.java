package com.rexcantor64.triton.language.parser;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LegacyParserTest {

    private final AdventureParser parser = new AdventureParser();

    @Test
    public void testSerializingComponent() {
        Component component = Component.text("Lorem ")
                .append(
                        Component.text("ipsum dolor ")
                                .color(NamedTextColor.BLACK)
                                .decorate(TextDecoration.BOLD)
                )
                .append(
                        Component.text("sit amet,")
                                .decorate(TextDecoration.BOLD)
                )
                .append(
                        Component.text(" consectetur ")
                                .color(TextColor.color(0xaabbcc))
                                .clickEvent(ClickEvent.copyToClipboard("some text"))
                                .append(
                                        Component.text("adipiscing ")
                                                .font(Key.key("default"))
                                )
                                .append(
                                        Component.text("elit. ")
                                                .hoverEvent(HoverEvent.showText(Component.text("hello world")))
                                )
                )
                .append(
                        Component.text("Maecenas imperdiet ")
                                .color(NamedTextColor.AQUA)
                                .append(
                                        Component.translatable(
                                                "some.key",
                                                Component.text("arg 1")
                                        )
                                )
                );

        LegacyParser.SerializedComponent serializedComponent = new LegacyParser.SerializedComponent(component);

        assertEquals(1, serializedComponent.getClickEvents().size());
        assertEquals(1, serializedComponent.getHoverEvents().size());
        assertEquals(1, serializedComponent.getTranslatableComponents().size());
        assertEquals(
                "§rLorem §0§lipsum dolor §r§lsit amet,§x§a§a§b§b§c§c\uE4005"
                + serializedComponent.getClickEvents().keySet().iterator().next()
                + " consectetur §x§a§a§b§b§c§c\uE800minecraft:default\uE802adipiscing \uE801§x§a§a§b§b§c§c\uE500"
                + serializedComponent.getHoverEvents().keySet().iterator().next()
                + "elit. \uE501\uE401§bMaecenas imperdiet §b\uE600some.key\uE600"
                + serializedComponent.getTranslatableComponents().keySet().iterator().next()
                + "\uE600",
                serializedComponent.getText()
        );
    }
}
