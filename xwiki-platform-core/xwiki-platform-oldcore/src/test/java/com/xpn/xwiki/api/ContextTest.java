/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.api;

import org.junit.Rule;
import org.junit.Test;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.test.annotation.AllComponents;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.classes.BaseClass;
import com.xpn.xwiki.test.MockitoOldcoreRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Context}.
 *
 * @version $Id$
 * @since 4.4RC1
 */
@AllComponents
public class ContextTest
{
    @Rule
    public MockitoOldcoreRule oldcoreRule = new MockitoOldcoreRule();

    /**
     * Tests that pages can override the default property display mode using {@code $xcontext.setDisplayMode}.
     *
     * @see "XWIKI-2436"
     */
    @Test
    public void setDisplayMode() throws Exception
    {
        XWikiContext xcontext = this.oldcoreRule.getXWikiContext();

        DocumentReference documentReference = new DocumentReference("wiki", "space", "page");
        XWikiDocument document = new XWikiDocument(documentReference);
        BaseClass baseClass = document.getXClass();

        doReturn("xwiki/2.1").when(this.oldcoreRule.getSpyXWiki()).getCurrentContentSyntaxId("xwiki/2.1", xcontext);
        doReturn(baseClass).when(this.oldcoreRule.getSpyXWiki()).getXClass(documentReference, xcontext);

        baseClass.addTextField("prop", "prop", 5);
        BaseObject obj = (BaseObject) document.getXClass().newObject(xcontext);
        obj.setStringValue("prop", "value");
        document.addXObject(obj);

        Context context = new Context(xcontext);
        context.setDisplayMode("edit");

        // We verify that the result contains a form input
        assertEquals("<input size='5' id='space.page_0_prop' value='value' name='space.page_0_prop' "
            + "type='text'/>", document.display("prop", xcontext));
    }
}
