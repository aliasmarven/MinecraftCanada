# Known Issue: Maple Tree Generation Collisions

**Project:** Unofficial Canadian Content (UCC)  
**Minecraft:** Java Edition 26.3  
**Discovered:** Beta 3 development  
**Status:** Open — deferred  
**Severity:** Minor visual/world-generation issue

## Description

Naturally generated maple trees sometimes appear immediately adjacent
to other trees, particularly vanilla birch trees, within Maple Forests.

Observed behavior includes:
- Maple and birch trunks directly touching.
- Maple trees generating unusually close to neighboring trees.
- Merged or heavily overlapping foliage.
- Unnatural double-tree arrangements.

The issue occurs frequently enough to be noticeable during exploration.

## Investigation

The natural maple tree feature is:

`data/canadamod/worldgen/feature/maple_leaf_litter.json`

Its original clearance configuration is:

- `limit`: 1
- `lower_size`: 0
- `upper_size`: 1

Two experimental adjustments were tested during Beta 3.

### Experiment 1

- Lower clearance: 0
- Upper clearance: 2

Result: Adjacent trunks persisted. Collisions appeared more frequent
to the tester, although this was not measured using identical seeds.

### Experiment 2

- Lower clearance: 2
- Upper clearance: 1

Result: Adjacent trunks persisted, and the overall tree arrangement
appeared less natural.

Neither experiment produced a satisfactory improvement.

**Both experimental changes were reverted.**

## Potential Future Fixes

- Investigate generation order between maple and vanilla tree features.
- Examine Minecraft 26.3 tree placement and clearance behavior.
- Consider a targeted nearby-trunk exclusion check.
- Compare identical seeds under controlled test conditions.
- Preserve the Maple Forest's density and natural canopy overlap.

## Intended Behavior

Dense forests with natural foliage overlap, but without frequent
directly adjacent trunks forming unnatural double-tree combinations.

## Release Decision

Deferred to future world-generation work.

No experimental tree-clearance adjustments are included in Beta 3.
