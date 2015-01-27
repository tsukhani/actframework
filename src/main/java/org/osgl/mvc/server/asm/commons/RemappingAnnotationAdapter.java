/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2011 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.osgl.mvc.server.asm.commons;

/**
 * An {@link org.osgl.mvc.server.asm.AnnotationVisitor} adapter for type remapping.
 * 
 * @author Eugene Kuleshov
 */
public class RemappingAnnotationAdapter extends org.osgl.mvc.server.asm.AnnotationVisitor {

    protected final org.osgl.mvc.server.asm.commons.Remapper remapper;

    public RemappingAnnotationAdapter(final org.osgl.mvc.server.asm.AnnotationVisitor av,
            final org.osgl.mvc.server.asm.commons.Remapper remapper) {
        this(org.osgl.mvc.server.asm.Opcodes.ASM5, av, remapper);
    }

    protected RemappingAnnotationAdapter(final int api,
            final org.osgl.mvc.server.asm.AnnotationVisitor av, final org.osgl.mvc.server.asm.commons.Remapper remapper) {
        super(api, av);
        this.remapper = remapper;
    }

    @Override
    public void visit(String name, Object value) {
        av.visit(name, remapper.mapValue(value));
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        av.visitEnum(name, remapper.mapDesc(desc), value);
    }

    @Override
    public org.osgl.mvc.server.asm.AnnotationVisitor visitAnnotation(String name, String desc) {
        org.osgl.mvc.server.asm.AnnotationVisitor v = av.visitAnnotation(name, remapper.mapDesc(desc));
        return v == null ? null : (v == av ? this
                : new RemappingAnnotationAdapter(v, remapper));
    }

    @Override
    public org.osgl.mvc.server.asm.AnnotationVisitor visitArray(String name) {
        org.osgl.mvc.server.asm.AnnotationVisitor v = av.visitArray(name);
        return v == null ? null : (v == av ? this
                : new RemappingAnnotationAdapter(v, remapper));
    }
}