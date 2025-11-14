#!/bin/bash

echo "========================================="
echo "Android Project Structure Validation"
echo "========================================="
echo ""

# Count files
echo "📁 File Count:"
echo "  Kotlin files: $(find app/src/main/java -name "*.kt" | wc -l)"
echo "  XML layouts: $(find app/src/main/res/layout -name "*.xml" | wc -l)"
echo "  Value resources: $(find app/src/main/res/values -name "*.xml" | wc -l)"
echo "  Drawable resources: $(find app/src/main/res/drawable -name "*.xml" | wc -l)"
echo ""

# Check key files
echo "✅ Key Files Check:"
files=(
    "app/build.gradle.kts"
    "app/src/main/AndroidManifest.xml"
    "app/src/main/java/com/josemanuelfernandez/appv1/MainActivity.kt"
    "app/src/main/java/com/josemanuelfernandez/appv1/data/database/AppDatabase.kt"
    "app/src/main/res/layout/activity_main.xml"
    "app/src/main/res/navigation/nav_graph.xml"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "  ✓ $file"
    else
        echo "  ✗ $file (MISSING)"
    fi
done
echo ""

# Check packages
echo "📦 Package Structure:"
packages=(
    "app/src/main/java/com/josemanuelfernandez/appv1/data/entity"
    "app/src/main/java/com/josemanuelfernandez/appv1/data/dao"
    "app/src/main/java/com/josemanuelfernandez/appv1/data/database"
    "app/src/main/java/com/josemanuelfernandez/appv1/data/repository"
    "app/src/main/java/com/josemanuelfernandez/appv1/viewmodel"
    "app/src/main/java/com/josemanuelfernandez/appv1/ui/facebook"
    "app/src/main/java/com/josemanuelfernandez/appv1/ui/reddit"
)

for package in "${packages[@]}"; do
    if [ -d "$package" ]; then
        file_count=$(find "$package" -maxdepth 1 -name "*.kt" | wc -l)
        echo "  ✓ ${package##*/} ($file_count files)"
    else
        echo "  ✗ ${package##*/} (MISSING)"
    fi
done
echo ""

# Check entities
echo "🗄️  Database Entities:"
for entity in User Comment Post Vote; do
    file="app/src/main/java/com/josemanuelfernandez/appv1/data/entity/${entity}.kt"
    if [ -f "$file" ]; then
        echo "  ✓ $entity"
    else
        echo "  ✗ $entity (MISSING)"
    fi
done
echo ""

# Check DAOs
echo "🔍 Data Access Objects:"
for dao in User Comment Post Vote; do
    file="app/src/main/java/com/josemanuelfernandez/appv1/data/dao/${dao}Dao.kt"
    if [ -f "$file" ]; then
        echo "  ✓ ${dao}Dao"
    else
        echo "  ✗ ${dao}Dao (MISSING)"
    fi
done
echo ""

# Check ViewModels
echo "🎯 ViewModels:"
for vm in Facebook Reddit; do
    file="app/src/main/java/com/josemanuelfernandez/appv1/viewmodel/${vm}ViewModel.kt"
    if [ -f "$file" ]; then
        echo "  ✓ ${vm}ViewModel"
    else
        echo "  ✗ ${vm}ViewModel (MISSING)"
    fi
done
echo ""

# Check layouts
echo "📱 Layouts:"
layouts=(
    "activity_main.xml"
    "fragment_facebook.xml"
    "fragment_reddit.xml"
    "item_facebook_comment.xml"
    "item_reddit_post.xml"
    "item_reddit_comment.xml"
)

for layout in "${layouts[@]}"; do
    file="app/src/main/res/layout/$layout"
    if [ -f "$file" ]; then
        echo "  ✓ $layout"
    else
        echo "  ✗ $layout (MISSING)"
    fi
done
echo ""

# Check resources
echo "🎨 Resources:"
resources=(
    "app/src/main/res/values/strings.xml"
    "app/src/main/res/values/colors.xml"
    "app/src/main/res/values/themes.xml"
    "app/src/main/res/navigation/nav_graph.xml"
    "app/src/main/res/menu/bottom_nav_menu.xml"
)

for resource in "${resources[@]}"; do
    if [ -f "$resource" ]; then
        echo "  ✓ ${resource##*/}"
    else
        echo "  ✗ ${resource##*/} (MISSING)"
    fi
done
echo ""

# Count lines of code
echo "📊 Lines of Code:"
kotlin_lines=$(find app/src/main/java -name "*.kt" -exec cat {} \; | wc -l)
xml_lines=$(find app/src/main/res -name "*.xml" -exec cat {} \; | wc -l)
echo "  Kotlin: $kotlin_lines lines"
echo "  XML: $xml_lines lines"
echo "  Total: $((kotlin_lines + xml_lines)) lines"
echo ""

echo "========================================="
echo "✅ Project structure validation complete!"
echo "========================================="
