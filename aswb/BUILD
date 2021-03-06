#
# Description: Builds ASwB for blaze and bazel
#

licenses(["notice"])  # Apache 2.0

load(
    "//build_defs:build_defs.bzl",
    "intellij_plugin",
    "merged_plugin_xml",
    "optional_plugin_xml",
    "plugin_deploy_zip",
    "repackaged_files",
    "stamped_plugin_xml",
)
load(
    "//build_defs:intellij_plugin_debug_target.bzl",
    "intellij_plugin_debug_target",
)
load("//:version.bzl", "VERSION")
load("//intellij_platform_sdk:build_defs.bzl", "select_for_plugin_api")
load(
    "//testing:test_defs.bzl",
    "intellij_integration_test_suite",
    "intellij_unit_test_suite",
)

merged_plugin_xml(
    name = "merged_plugin_xml_common",
    srcs = [
        "src/META-INF/aswb.xml",
        "//base:plugin_xml",
        "//java:plugin_xml",
    ],
)

optional_plugin_xml(
    name = "optional_cpp_xml",
    module = "com.intellij.modules.cidr.lang",
    plugin_xml = "//cpp:plugin_xml",
)

optional_plugin_xml(
    name = "optional_ndk_xml",
    module = "com.android.tools.ndk",
    plugin_xml = ":merged_ndk_contents_xml",
)

merged_plugin_xml(
    name = "merged_ndk_contents_xml",
    srcs = ["src/META-INF/ndk-contents.xml"] + select_for_plugin_api({
        "android-studio-2.3.1.0": [],
        "android-studio-3.0.0.9": ["3.0/src/META-INF/ndk-workspace-contents.xml"],
    }),
)

OPTIONAL_PLUGIN_XMLS = [
    "//java:optional_xml",
    ":optional_cpp_xml",
    ":optional_ndk_xml",
]

merged_plugin_xml(
    name = "merged_plugin_xml",
    srcs = [
        "src/META-INF/aswb_bazel.xml",
        ":merged_plugin_xml_common",
    ],
)

stamped_plugin_xml(
    name = "stamped_plugin_xml",
    changelog_file = "//:changelog",
    include_product_code_in_stamp = True,
    plugin_id = "com.google.idea.bazel.aswb",
    plugin_name = "Bazel",
    plugin_xml = ":merged_plugin_xml",
    stamp_since_build = True,
    version = VERSION,
)

java_library(
    name = "aswb_lib",
    srcs = glob(["src/**/*.java"]) + select_for_plugin_api({
        "android-studio-2.3.1.0": glob(["2.3/src/**/*.java"]),
        "android-studio-3.0.0.9": glob(["3.0/src/**/*.java"]),
    }),
    resources = glob(["resources/**/*"]),
    deps = [
        "//base",
        "//common/experiments",
        "//cpp",
        "//intellij_platform_sdk:plugin_api",
        "//java",
        "//proto:proto_deps",
        "//sdkcompat",
        "@jsr305_annotations//jar",
    ],
)

java_library(
    name = "integration_test_utils",
    testonly = 1,
    srcs = glob(["tests/utils/integration/**/*.java"]),
    deps = [
        "@jsr305_annotations//jar",
        "@junit//jar",
    ],
)

intellij_unit_test_suite(
    name = "unit_tests",
    srcs = glob(["tests/unittests/**/*.java"]) + select_for_plugin_api({
        "android-studio-2.3.1.0": [],
        "android-studio-3.0.0.9": glob(["3.0/tests/unittests/**/*.java"]),
    }),
    test_package_root = "com.google.idea.blaze.android",
    deps = [
        ":aswb_lib",
        "//base",
        "//base:unit_test_utils",
        "//common/experiments",
        "//common/experiments:unit_test_utils",
        "//intellij_platform_sdk:plugin_api_for_tests",
        "//java",
        "//proto:proto_deps",
        "@jsr305_annotations//jar",
        "@junit//jar",
    ],
)

test_suite(
    name = "integration_tests",
    tests = [
        ":NdkDependenciesTest",
        ":normal_integration_tests",
    ],
)

intellij_integration_test_suite(
    name = "normal_integration_tests",
    srcs = glob(
        ["tests/integrationtests/**/*.java"],
        exclude = ["tests/integrationtests/com/google/idea/blaze/android/plugin/NdkDependenciesTest.java"],
    ),
    platform_prefix = "AndroidStudio",
    required_plugins = "com.google.idea.bazel.aswb",
    test_package_root = "com.google.idea.blaze.android",
    runtime_deps = [
        ":aswb_bazel",
    ],
    deps = [
        ":aswb_lib",
        ":integration_test_utils",
        "//base",
        "//base:integration_test_utils",
        "//base:unit_test_utils",
        "//common/experiments",
        "//common/experiments:unit_test_utils",
        "//cpp",
        "//intellij_platform_sdk:plugin_api_for_tests",
        "//java",
        "//proto:proto_deps",
        "//sdkcompat",
        "@jsr305_annotations//jar",
        "@junit//jar",
    ],
)

intellij_integration_test_suite(
    name = "NdkDependenciesTest",
    srcs = ["tests/integrationtests/com/google/idea/blaze/android/plugin/NdkDependenciesTest.java"],
    platform_prefix = "AndroidStudio",
    required_plugins = "com.google.idea.bazel.aswb",
    test_package_root = "com.google.idea.blaze.android",
    runtime_deps = [
        ":aswb_bazel",
        "//cpp",
        "//java",
    ],
    deps = [
        ":aswb_lib",
        ":integration_test_utils",
        "//base",
        "//base:integration_test_utils",
        "//base:unit_test_utils",
        "//intellij_platform_sdk:plugin_api_for_tests",
        "//proto:proto_deps",
        "@junit//jar",
    ],
)

intellij_plugin(
    name = "aswb_bazel",
    optional_plugin_xmls = OPTIONAL_PLUGIN_XMLS,
    plugin_xml = ":stamped_plugin_xml",
    deps = [
        ":aswb_lib",
    ],
)

repackaged_files(
    name = "plugin_jar",
    srcs = [":aswb_bazel"],
    prefix = "aswb/lib",
)

repackaged_files(
    name = "aspect_directory",
    srcs = ["//aspect:aspect_files"],
    prefix = "aswb/aspect",
)

intellij_plugin_debug_target(
    name = "aswb_bazel_dev",
    deps = [
        ":aspect_directory",
        ":plugin_jar",
    ],
)

plugin_deploy_zip(
    name = "aswb_bazel_zip",
    srcs = [
        ":aspect_directory",
        ":plugin_jar",
    ],
    zip_filename = "aswb_bazel.zip",
)
