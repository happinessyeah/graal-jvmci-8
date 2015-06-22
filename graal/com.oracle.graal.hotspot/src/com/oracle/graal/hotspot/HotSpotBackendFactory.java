/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.hotspot;

import jdk.internal.jvmci.hotspot.HotSpotJVMCIRuntime.*;
import jdk.internal.jvmci.runtime.*;

public interface HotSpotBackendFactory {

    HotSpotBackend createBackend(HotSpotGraalRuntimeProvider runtime, JVMCIBackend jvmciBackend, HotSpotBackend host);

    /**
     * Gets the CPU architecture of this backend.
     */
    String getArchitecture();

    /**
     * Gets the name of the {@link Options#JVMCIRuntime JVMCIRuntime} in which the backend created
     * by this factory should be used.
     */
    String getGraalRuntimeName();

}
