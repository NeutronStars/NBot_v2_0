package fr.neutronstars.nbot.util;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Message;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ImageBuilder
{
    private final BufferedImage image;
    private final Graphics graphics;
    private final File folder;

    public ImageBuilder(Guild guild, int width, int length)
    {
        image = new BufferedImage(width, length, BufferedImage.TYPE_INT_ARGB);
        graphics = image.getGraphics();
        folder = new File("tmp/"+guild.getId());

        if(!folder.exists()) folder.mkdir();
    }

    public Graphics getGraphics()
    {
        return graphics;
    }

    public ImageBuilder setBackground(Image image)
    {
        getGraphics().drawImage(image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
        return this;
    }

    public ImageBuilder setColor(Color color)
    {
        getGraphics().setColor(color);
        return this;
    }

    public ImageBuilder setFont(Font font)
    {
        getGraphics().setFont(font);
        return this;
    }

    public int getWidth()
    {
        return image.getWidth();
    }

    public int getHeight()
    {
        return image.getHeight();
    }

    public ImageBuilder drawString(String text, float x, float y, Font font, Color color)
    {
        Graphics graphics = getGraphics();
        graphics.setColor(color);
        graphics.setFont(font);

        Rectangle2D rect = getTextBox(text);

        float px = ((getWidth() / 100.0f) * x) - ((float) rect.getWidth() / 2.0f);
        float py = ((getHeight() / 100.0f) * y) + ((float) rect.getHeight() / 2.0f);

        graphics.drawString(text, (int) px, (int) py);
        return this;
    }

    public ImageBuilder fillRect(int x, int y, int width, int height)
    {
        return fillRect(x, y, width, height, 1);
    }

    public ImageBuilder fillRect(int x, int y, int width, int height, int borderSize)
    {
        for(int i = 0; i < borderSize; i++)
            graphics.fillRect(x+i, y+i, width - (i*2), height - (i*2));
        return this;
    }

    public ImageBuilder drawString(String text, int x, int y, Color color)
    {
        getGraphics().setColor(color);
        getGraphics().drawString(text, x, y);
        return this;
    }

    public ImageBuilder drawImage(Image image, int x, int y, int width, int height)
    {
        getGraphics().drawImage(image, x, y, width, height, null);
        return this;
    }

    public Rectangle2D getTextBox(String text)
    {
        FontRenderContext context = new FontRenderContext(getGraphics().getFont().getTransform(), false, false);
        return getGraphics().getFont().getStringBounds(text, context);
    }

    private File buildFile()
    {
        File file = createFile(folder);

        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            NBot.getLogger().error(e.getMessage(), e);
        }

        return file;
    }

    private static File createFile(File folder)
    {
        File file = null;

        int x = 0;

        do {
            file = new File(folder, "image-creator-"+x+".png");
            x++;
        }while (file.exists());

        return file;
    }

    public void sendChannel(MessageChannel channel, net.dv8tion.jda.core.entities.Message message)
    {
        File file = buildFile();
        channel.sendFile(file, message).queue(msg -> file.delete());
    }

    public void sendChannel(MessageChannel channel)
    {
        sendChannel(channel, (net.dv8tion.jda.core.entities.Message) null);
    }

    public void sendChannel(MessageChannel channel, String message)
    {
        sendChannel(channel, new MessageBuilder(message).build());
    }

    public static Image getImageURL(Guild guild, String sourceUrl)
    {
        URL imageUrl;
        URLConnection uc = null;
        try {
            imageUrl = new URL(sourceUrl);
            uc = imageUrl.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File folder = new File("tmp/"+guild.getId());
        if(!folder.exists()) folder.mkdir();

        File file = createFile(folder);

        try (InputStream imageReader = new BufferedInputStream(uc.getInputStream());
             OutputStream imageWriter = new BufferedOutputStream(new FileOutputStream(file))){
            int readByte;
            while ((readByte = imageReader.read()) != -1) {
                imageWriter.write(readByte);
            }
            imageWriter.flush();
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Image image = new ImageIcon(file.getAbsolutePath()).getImage();
        file.delete();

        return image;
    }
}
