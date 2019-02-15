/*
 * Copyright (c) 2009, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.vm.ci.hotspot;

import static jdk.vm.ci.hotspot.HotSpotJVMCIRuntime.runtime;

import jdk.vm.ci.meta.Assumptions;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.ResolvedJavaType;

/**
 * Represents a constant non-{@code null} object reference, within the compiler and across the
 * compiler/runtime interface.
 */
abstract class HotSpotObjectConstantImpl implements HotSpotObjectConstant {

    protected final boolean compressed;

    HotSpotObjectConstantImpl(boolean compressed) {
        this.compressed = compressed;
    }

    @Override
    public JavaKind getJavaKind() {
        return JavaKind.Object;
    }

    @Override
    public boolean isCompressed() {
        return compressed;
    }

    @Override
    public abstract JavaConstant compress();

    @Override
    public abstract JavaConstant uncompress();

    @Override
    public HotSpotResolvedObjectType getType() {
        return runtime().reflection.getType(this);
    }

    @Override
    public abstract int getIdentityHashCode();

    @Override
    public JavaConstant getCallSiteTarget(Assumptions assumptions) {
        if (runtime().getCallSite().isInstance(this)) {
            HotSpotObjectConstantImpl target = (HotSpotObjectConstantImpl) runtime().getHostJVMCIBackend().getConstantReflection().readFieldValue(
                            HotSpotMethodHandleAccessProvider.Internals.instance().callSiteTargetField, this);
            if (!runtime().getConstantCallSite().isInstance(this)) {
                if (assumptions == null) {
                    return null;
                }
                assumptions.record(new Assumptions.CallSiteTargetValue(this, target));
            }
            return target;
        }
        return null;
    }

    @Override
    public boolean isInternedString() {
        return runtime().compilerToVm.isInternedString(this);
    }

    @Override
    public <T> T asObject(Class<T> type) {
        return runtime().reflection.asObject(this, type);
    }

    @Override
    public Object asObject(ResolvedJavaType type) {
        return runtime().reflection.asObject(this, (HotSpotResolvedJavaType) type);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isDefaultForKind() {
        return false;
    }

    @Override
    public Object asBoxedPrimitive() {
        throw new IllegalArgumentException();
    }

    @Override
    public int asInt() {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean asBoolean() {
        throw new IllegalArgumentException();
    }

    @Override
    public long asLong() {
        throw new IllegalArgumentException();
    }

    @Override
    public float asFloat() {
        throw new IllegalArgumentException();
    }

    @Override
    public double asDouble() {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof HotSpotObjectConstantImpl) {
            HotSpotObjectConstantImpl other = (HotSpotObjectConstantImpl) o;
            return runtime().reflection.equals(this, other);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getIdentityHashCode();
    }

    @Override
    public String toValueString() {
        if (runtime().getJavaLangString().isInstance(this)) {
            return "\"" + runtime().reflection.asString(this) + "\"";
        } else {
            return runtime().reflection.formatString(this);
        }
    }

    @Override
    public String toString() {
        return (compressed ? "NarrowOop" : getJavaKind().getJavaName()) + "[" + runtime().reflection.formatString(this) + "]";
    }

    public JavaConstant readFieldValue(HotSpotResolvedJavaField field, boolean isVolatile) {
        return runtime().reflection.readFieldValue(this, field, isVolatile);
    }

    public ResolvedJavaType asJavaType() {
        return runtime().reflection.asJavaType(this);
    }
}
