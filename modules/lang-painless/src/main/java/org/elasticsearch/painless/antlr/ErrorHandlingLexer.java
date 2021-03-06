/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.painless.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.misc.Interval;

import java.text.ParseException;

/**
 * A lexer that will override the default error behavior to fail on the first error.
 */
final class ErrorHandlingLexer extends PainlessLexer {

    ErrorHandlingLexer(final CharStream charStream) {
        super(charStream);
    }

    @Override
    public void recover(final LexerNoViableAltException lnvae) {
        final CharStream charStream = lnvae.getInputStream();
        final int startIndex = lnvae.getStartIndex();
        final String text = charStream.getText(Interval.of(startIndex, charStream.index()));

        final ParseException parseException = new ParseException("Error [" + _tokenStartLine + ":" +
                _tokenStartCharPositionInLine + "]: unexpected character [" +
                getErrorDisplay(text) + "].",  _tokenStartCharIndex);
        parseException.initCause(lnvae);

        throw new RuntimeException(parseException);
    }
}
