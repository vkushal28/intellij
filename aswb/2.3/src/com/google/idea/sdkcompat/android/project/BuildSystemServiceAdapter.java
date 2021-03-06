/*
 * Copyright 2017 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.sdkcompat.android.project;

import com.android.tools.idea.npw.project.AndroidSourceSet;
import com.android.tools.idea.project.BuildSystemService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.List;
import javax.annotation.Nullable;
import org.jetbrains.android.facet.AndroidFacet;

/** Compatibility adapter for {@link BuildSystemService}. */
public abstract class BuildSystemServiceAdapter extends BuildSystemService {
  public abstract String mergeBuildFiles(
      String dependencies,
      String destinationContents,
      Project project,
      @Nullable String supportLibVersionFilter);

  public abstract List<AndroidSourceSet> getSourceSets(
      AndroidFacet facet, @Nullable VirtualFile targetDirectory);
}
