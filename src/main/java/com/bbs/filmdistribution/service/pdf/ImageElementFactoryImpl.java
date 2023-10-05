package com.bbs.filmdistribution.service.pdf;

import com.lowagie.text.Image;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation to build an image into a pdf file.
 */
public class ImageElementFactoryImpl implements ReplacedElementFactory
{
    private static final Logger LOGGER = Logger.getLogger( ImageElementFactoryImpl.class.getName() );

    @Override
    public ReplacedElement createReplacedElement( LayoutContext lc, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight )
    {
        Element element = box.getElement();
        if ( element.getNodeName().equals( "img" ) )
        {
            String imagePath = element.getAttribute( "src" );
            try
            {
                ClassPathResource resource = new ClassPathResource( imagePath );
                InputStream input = resource.getInputStream();
                byte[] bytes = IOUtils.toByteArray( input );
                Image image = Image.getInstance( bytes );
                FSImage fsImage = new ITextFSImage( image );
                if ( cssWidth != -1 || cssHeight != -1 )
                {
                    fsImage.scale( cssWidth, cssHeight );
                }
                else
                {
                    fsImage.scale( 2000, 1000 );
                }
                return new ITextImageElement( fsImage );
            }
            catch ( Exception e )
            {
                LOGGER.log( Level.WARNING, e.getMessage() );
            }
        }
        return null;
    }

    @Override
    public void reset()
    {
        // Not implemented
    }

    @Override
    public void remove( Element element )
    {
        // Not implemented
    }

    @Override
    public void setFormSubmissionListener( FormSubmissionListener formSubmissionListener )
    {
        // Not implemented
    }
}
