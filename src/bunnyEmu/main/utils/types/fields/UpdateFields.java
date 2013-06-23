package bunnyEmu.main.utils.types.fields;

/**
 * UpdateFields for MoP 5.1.0
 * 
 * @author Marijn
 *
 */
public class UpdateFields {

	 public class ObjectFields
	    {
	    	public static final int Guid                              = 0x0;
	    	public static final int Data                              = 0x2;
	    	public static final int Type                              = 0x4;
	    	public static final int Entry                             = 0x5;
	    	public static final int Scale                             = 0x6;
	    	public static final int End                               = 0x7;
	    };

	    public class DynamicObjectArrays
	    {
	    	// Empty
	    	public static final int End                               = 0x0;
	    }

	    public class ItemFields
	    {
	    	public static final int Owner                             = ObjectFields.End + 0x0;
	    	public static final int ContainedIn                       = ObjectFields.End + 0x2;
	    	public static final int Creator                           = ObjectFields.End + 0x4;
	    	public static final int GiftCreator                       = ObjectFields.End + 0x6;
	    	public static final int StackCount                        = ObjectFields.End + 0x8;
	    	public static final int Expiration                        = ObjectFields.End + 0x9;
	    	public static final int SpellCharges                      = ObjectFields.End + 0xA;
	    	public static final int DynamicFlags                      = ObjectFields.End + 0xF;
	    	public static final int Enchantment                       = ObjectFields.End + 0x10;
	    	public static final int PropertySeed                      = ObjectFields.End + 0x37;
	    	public static final int RandomPropertiesID                = ObjectFields.End + 0x38;
	    	public static final int Durability                        = ObjectFields.End + 0x39;
	    	public static final int MaxDurability                     = ObjectFields.End + 0x3A;
	    	public static final int CreatePlayedTime                  = ObjectFields.End + 0x3B;
	    	public static final int ModifiersMask                     = ObjectFields.End + 0x3C;
	    	public static final int End                               = ObjectFields.End + 0x3D;
	    };

	    public class ItemDynamicArrays
	    {
	    	// Empty
	    	public static final int End                               = DynamicObjectArrays.End;
	    }

	    public class ContainerFields
	    {
	    	public static final int NumSlots                          = ItemFields.End + 0x0;
	    	public static final int Slots                             = ItemFields.End + 0x1;
	    	public static final int End                               = ItemFields.End + 0x49;
	    };

	    public class ContainerDynamicArrays
	    {
	    	// Empty
	    	public static final int End                               = ItemDynamicArrays.End;
	    }

	    public class UnitFields
	    {
	    	public static final int Charm                             = ObjectFields.End + 0x0;
	    	public static final int Summon                            = ObjectFields.End + 0x2;
	    	public static final int Critter                           = ObjectFields.End + 0x4;
	    	public static final int CharmedBy                         = ObjectFields.End + 0x6;
	    	public static final int SummonedBy                        = ObjectFields.End + 0x8;
	    	public static final int CreatedBy                         = ObjectFields.End + 0xA;
	    	public static final int Target                            = ObjectFields.End + 0xC;
	    	public static final int ChannelObject                     = ObjectFields.End + 0xE;
	    	public static final int SummonedByHomeRealm               = ObjectFields.End + 0x10;
	    	public static final int ChannelSpell                      = ObjectFields.End + 0x11;
	    	public static final int DisplayPower                      = ObjectFields.End + 0x12;
	    	public static final int OverrideDisplayPowerID            = ObjectFields.End + 0x13;
	    	public static final int Health                            = ObjectFields.End + 0x14;
	    	public static final int Power                             = ObjectFields.End + 0x15;
	    	public static final int MaxHealth                         = ObjectFields.End + 0x1A;
	    	public static final int MaxPower                          = ObjectFields.End + 0x1B;
	    	public static final int PowerRegenFlatModifier            = ObjectFields.End + 0x20;
	    	public static final int PowerRegenInterruptedFlatModifier = ObjectFields.End + 0x25;
	    	public static final int Level                             = ObjectFields.End + 0x2A;
	    	public static final int FactionTemplate                   = ObjectFields.End + 0x2B;
	    	public static final int VirtualItemID                     = ObjectFields.End + 0x2C;
	    	public static final int Flags                             = ObjectFields.End + 0x2F;
	    	public static final int Flags2                            = ObjectFields.End + 0x30;
	    	public static final int AuraState                         = ObjectFields.End + 0x31;
	    	public static final int AttackRoundBaseTime               = ObjectFields.End + 0x32;
	    	public static final int RangedAttackRoundBaseTime         = ObjectFields.End + 0x34;
	    	public static final int BoundingRadius                    = ObjectFields.End + 0x35;
	    	public static final int CombatReach                       = ObjectFields.End + 0x36;
	    	public static final int DisplayID                         = ObjectFields.End + 0x37;
	    	public static final int NativeDisplayID                   = ObjectFields.End + 0x38;
	    	public static final int MountDisplayID                    = ObjectFields.End + 0x39;
	    	public static final int MinDamage                         = ObjectFields.End + 0x3A;
	    	public static final int MaxDamage                         = ObjectFields.End + 0x3B;
	    	public static final int MinOffHandDamage                  = ObjectFields.End + 0x3C;
	    	public static final int MaxOffHandDamage                  = ObjectFields.End + 0x3D;
	    	public static final int AnimTier                          = ObjectFields.End + 0x3E;
	    	public static final int PetNumber                         = ObjectFields.End + 0x3F;
	    	public static final int PetNameTimestamp                  = ObjectFields.End + 0x40;
	    	public static final int PetExperience                     = ObjectFields.End + 0x41;
	    	public static final int PetNextLevelExperience            = ObjectFields.End + 0x42;
	    	public static final int DynamicFlags                      = ObjectFields.End + 0x43;
	    	public static final int ModCastingSpeed                   = ObjectFields.End + 0x44;
	    	public static final int ModSpellHaste                     = ObjectFields.End + 0x45;
	    	public static final int ModHaste                          = ObjectFields.End + 0x46;
	    	public static final int ModHasteRegen                     = ObjectFields.End + 0x47;
	    	public static final int CreatedBySpell                    = ObjectFields.End + 0x48;
	    	public static final int NpcFlags                          = ObjectFields.End + 0x49;
	    	public static final int EmoteState                        = ObjectFields.End + 0x4B;
	    	public static final int Stats                             = ObjectFields.End + 0x4C;
	    	public static final int StatPosBuff                       = ObjectFields.End + 0x51;
	    	public static final int StatNegBuff                       = ObjectFields.End + 0x56;
	    	public static final int Resistances                       = ObjectFields.End + 0x5B;
	    	public static final int ResistanceBuffModsPositive        = ObjectFields.End + 0x62;
	    	public static final int ResistanceBuffModsNegative        = ObjectFields.End + 0x69;
	    	public static final int BaseMana                          = ObjectFields.End + 0x70;
	    	public static final int BaseHealth                        = ObjectFields.End + 0x71;
	    	public static final int ShapeshiftForm                    = ObjectFields.End + 0x72;
	    	public static final int AttackPower                       = ObjectFields.End + 0x73;
	    	public static final int AttackPowerModPos                 = ObjectFields.End + 0x74;
	    	public static final int AttackPowerModNeg                 = ObjectFields.End + 0x75;
	    	public static final int AttackPowerMultiplier             = ObjectFields.End + 0x76;
	    	public static final int RangedAttackPower                 = ObjectFields.End + 0x77;
	    	public static final int RangedAttackPowerModPos           = ObjectFields.End + 0x78;
	    	public static final int RangedAttackPowerModNeg           = ObjectFields.End + 0x79;
	    	public static final int RangedAttackPowerMultiplier       = ObjectFields.End + 0x7A;
	    	public static final int MinRangedDamage                   = ObjectFields.End + 0x7B;
	    	public static final int MaxRangedDamage                   = ObjectFields.End + 0x7C;
	    	public static final int PowerCostModifier                 = ObjectFields.End + 0x7D;
	    	public static final int PowerCostMultiplier               = ObjectFields.End + 0x84;
	    	public static final int MaxHealthModifier                 = ObjectFields.End + 0x8B;
	    	public static final int HoverHeight                       = ObjectFields.End + 0x8C;
	    	public static final int MinItemLevel                      = ObjectFields.End + 0x8D;
	    	public static final int MaxItemLevel                      = ObjectFields.End + 0x8E;
	    	public static final int WildBattlePetLevel                = ObjectFields.End + 0x8F;
	    	public static final int BattlePetCompanionGUID            = ObjectFields.End + 0x90;
	    	public static final int BattlePetCompanionNameTimestamp   = ObjectFields.End + 0x92;
	    	public static final int End                               = ObjectFields.End + 0x93;
	    }

	    public class UnitDynamicArrays
	    {
	    	public static final int PassiveSpells                     = DynamicObjectArrays.End + 0x0;
	    	public static final int End                               = DynamicObjectArrays.End + 0x101;
	    }

	    public class PlayerFields
	    {
	    	public static final int DuelArbiter                       = UnitFields.End + 0x0;
	    	public static final int PlayerFlags                       = UnitFields.End + 0x2;
	    	public static final int GuildRankID                       = UnitFields.End + 0x3;
	    	public static final int GuildDeleteDate                   = UnitFields.End + 0x4;
	    	public static final int GuildLevel                        = UnitFields.End + 0x5;
	    	public static final int HairColorID                       = UnitFields.End + 0x6;
	    	public static final int RestState                         = UnitFields.End + 0x7;
	    	public static final int ArenaFaction                      = UnitFields.End + 0x8;
	    	public static final int DuelTeam                          = UnitFields.End + 0x9;
	    	public static final int GuildTimeStamp                    = UnitFields.End + 0xA;
	    	public static final int QuestLog                          = UnitFields.End + 0xB;
	    	public static final int VisibleItems                      = UnitFields.End + 0x2F9;
	    	public static final int PlayerTitle                       = UnitFields.End + 0x31F;
	    	public static final int FakeInebriation                   = UnitFields.End + 0x320;
	    	public static final int HomePlayerRealm                   = UnitFields.End + 0x321;
	    	public static final int CurrentSpecID                     = UnitFields.End + 0x322;
	    	public static final int TaxiMountAnimKitID                = UnitFields.End + 0x323;
	    	public static final int CurrentBattlePetBreedQuality      = UnitFields.End + 0x324;
	    	public static final int InvSlots                          = UnitFields.End + 0x325;
	    	public static final int FarsightObject                    = UnitFields.End + 0x3D1;
	    	public static final int KnownTitles                       = UnitFields.End + 0x3D3;
	    	public static final int Coinage                           = UnitFields.End + 0x3DB;
	    	public static final int XP                                = UnitFields.End + 0x3DD;
	    	public static final int NextLevelXP                       = UnitFields.End + 0x3DE;
	    	public static final int Skill                             = UnitFields.End + 0x3DF;
	    	public static final int SpellCritPercentage               = UnitFields.End + 0x59F;
	    	public static final int CharacterPoints                   = UnitFields.End + 0x5A6;
	    	public static final int MaxTalentTiers                    = UnitFields.End + 0x5A7;
	    	public static final int TrackCreatureMask                 = UnitFields.End + 0x5A8;
	    	public static final int TrackResourceMask                 = UnitFields.End + 0x5A9;
	    	public static final int Expertise                         = UnitFields.End + 0x5AA;
	    	public static final int OffhandExpertise                  = UnitFields.End + 0x5AB;
	    	public static final int RangedExpertise                   = UnitFields.End + 0x5AC;
	    	public static final int BlockPercentage                   = UnitFields.End + 0x5AD;
	    	public static final int DodgePercentage                   = UnitFields.End + 0x5AE;
	    	public static final int ParryPercentage                   = UnitFields.End + 0x5AF;
	    	public static final int CritPercentage                    = UnitFields.End + 0x5B0;
	    	public static final int RangedCritPercentage              = UnitFields.End + 0x5B1;
	    	public static final int OffhandCritPercentage             = UnitFields.End + 0x5B2;
	    	public static final int ShieldBlock                       = UnitFields.End + 0x5B3;
	    	public static final int ShieldBlockCritPercentage         = UnitFields.End + 0x5B4;
	    	public static final int Mastery                           = UnitFields.End + 0x5B5;
	    	public static final int PvpPowerDamage                    = UnitFields.End + 0x5B6;
	    	public static final int PvpPowerHealing                   = UnitFields.End + 0x5B7;
	    	public static final int ExploredZones                     = UnitFields.End + 0x5B8;
	    	public static final int ModDamageDonePos                  = UnitFields.End + 0x680;
	    	public static final int ModDamageDoneNeg                  = UnitFields.End + 0x687;
	    	public static final int ModDamageDonePercent              = UnitFields.End + 0x68E;
	    	public static final int RestStateBonusPool                = UnitFields.End + 0x695;
	    	public static final int ModHealingDonePos                 = UnitFields.End + 0x696;
	    	public static final int ModHealingPercent                 = UnitFields.End + 0x697;
	    	public static final int ModHealingDonePercent             = UnitFields.End + 0x698;
	    	public static final int WeaponDmgMultipliers              = UnitFields.End + 0x699;
	    	public static final int ModPeriodicHealingDonePercent     = UnitFields.End + 0x69C;
	    	public static final int ModSpellPowerPercent              = UnitFields.End + 0x69D;
	    	public static final int ModResiliencePercent              = UnitFields.End + 0x69E;
	    	public static final int OverrideSpellPowerByAPPercent     = UnitFields.End + 0x69F;
	    	public static final int OverrideAPBySpellPowerPercent     = UnitFields.End + 0x6A0;
	    	public static final int ModTargetResistance               = UnitFields.End + 0x6A1;
	    	public static final int ModTargetPhysicalResistance       = UnitFields.End + 0x6A2;
	    	public static final int LifetimeMaxRank                   = UnitFields.End + 0x6A3;
	    	public static final int SelfResSpell                      = UnitFields.End + 0x6A4;
	    	public static final int PvpMedals                         = UnitFields.End + 0x6A5;
	    	public static final int BuybackPrice                      = UnitFields.End + 0x6A6;
	    	public static final int BuybackTimestamp                  = UnitFields.End + 0x6B2;
	    	public static final int YesterdayHonorableKills           = UnitFields.End + 0x6BE;
	    	public static final int LifetimeHonorableKills            = UnitFields.End + 0x6BF;
	    	public static final int WatchedFactionIndex               = UnitFields.End + 0x6C0;
	    	public static final int CombatRatings                     = UnitFields.End + 0x6C1;
	    	public static final int ArenaTeams                        = UnitFields.End + 0x6DC;
	    	public static final int RuneRegen                         = UnitFields.End + 0x6F1;
	    	public static final int NoReagentCostMask                 = UnitFields.End + 0x6F5;
	    	public static final int GlyphSlots                        = UnitFields.End + 0x6F9;
	    	public static final int Glyphs                            = UnitFields.End + 0x6FF;
	    	public static final int BattlegroundRating                = UnitFields.End + 0x705;
	    	public static final int MaxLevel                          = UnitFields.End + 0x706;
	    	public static final int GlyphSlotsEnabled                 = UnitFields.End + 0x707;
	    	public static final int Researching                       = UnitFields.End + 0x708;
	    	public static final int ProfessionSkillLine               = UnitFields.End + 0x710;
	    	public static final int PetSpellPower                     = UnitFields.End + 0x712;
	    	public static final int UiHitModifier                     = UnitFields.End + 0x713;
	    	public static final int UiSpellHitModifier                = UnitFields.End + 0x714;
	    	public static final int HomeRealmTimeOffset               = UnitFields.End + 0x715;
	    	public static final int ModRangedHaste                    = UnitFields.End + 0x716;
	    	public static final int ModPetHaste                       = UnitFields.End + 0x717;
	    	public static final int SummonedBattlePetGUID             = UnitFields.End + 0x718;
	    	public static final int OverrideSpellsID                  = UnitFields.End + 0x71A;
	    	public static final int End                               = UnitFields.End + 0x71B;
	    };

	    public class PlayerDynamicArrays
	    {
	    	public static final int ResearchSites                     = UnitDynamicArrays.End + 0x0;
	    	public static final int DailyQuestsCompleted              = UnitDynamicArrays.End + 0x2;
	    	public static final int End                               = UnitDynamicArrays.End + 0x4;
	    }

	    public class GameObjectFields
	    {
	    	public static final int CreatedBy                         = ObjectFields.End + 0x0;
	    	public static final int DisplayID                         = ObjectFields.End + 0x2;
	    	public static final int Flags                             = ObjectFields.End + 0x3;
	    	public static final int ParentRotation                    = ObjectFields.End + 0x4;
	    	public static final int AnimProgress                      = ObjectFields.End + 0x8;
	    	public static final int FactionTemplate                   = ObjectFields.End + 0x9;
	    	public static final int Level                             = ObjectFields.End + 0xA;
	    	public static final int PercentHealth                     = ObjectFields.End + 0xB;
	    	public static final int End                               = ObjectFields.End + 0xC;
	    };

	    public class GameObjectDynamicArrays
	    {
	    	// One field; unknown
	    	public static final int UnknownField                      = DynamicObjectArrays.End + 0x0;
	    	public static final int End                               = DynamicObjectArrays.End + 0x1;
	    }

	    public class DynamicObjectFields
	    {
	    	public static final int Caster                            = ObjectFields.End + 0x0;
	    	public static final int TypeAndVisualID                   = ObjectFields.End + 0x2;
	    	public static final int SpellId                           = ObjectFields.End + 0x3;
	    	public static final int Radius                            = ObjectFields.End + 0x4;
	    	public static final int CastTime                          = ObjectFields.End + 0x5;
	    	public static final int End                               = ObjectFields.End + 0x6;
	    };

	    public class DynamicObjectDynamicArrays
	    {
	    	// Empty
	    	public static final int End                               = DynamicObjectArrays.End;
	    }

	    public class CorpseFields
	    {
	    	public static final int Owner                             = ObjectFields.End + 0x0;
	    	public static final int PartyGuid                         = ObjectFields.End + 0x2;
	    	public static final int DisplayId                         = ObjectFields.End + 0x4;
	    	public static final int Items                             = ObjectFields.End + 0x5;
	    	public static final int SkinId                            = ObjectFields.End + 0x18;
	    	public static final int FacialHairStyleId                 = ObjectFields.End + 0x19;
	    	public static final int Flags                             = ObjectFields.End + 0x1A;
	    	public static final int DynamicFlags                      = ObjectFields.End + 0x1B;
	    	public static final int End                               = ObjectFields.End + 0x1C;
	    };

	    public class CorpseDynamicArrays
	    {
	    	// Empty
	    	public static final int End                               = DynamicObjectArrays.End;
	    }

	    public class AreaTriggerFields
	    {
	    	public static final int Caster                            = ObjectFields.End + 0x0;
	    	public static final int SpellId                           = ObjectFields.End + 0x2;
	    	public static final int SpellVisualId                     = ObjectFields.End + 0x3;
	    	public static final int Duration                          = ObjectFields.End + 0x4;
	    	public static final int End                               = ObjectFields.End + 0x5;
	    };

	    public class AreaTriggerDynamicArrays
	    {
	    	// Empty
	    	public static final int End                               = DynamicObjectArrays.End;
	    }

	    public class SceneObjectFields
	    {
	    	public static final int ScriptPackageId                   = ObjectFields.End + 0x0;
	    	public static final int RndSeedVal                        = ObjectFields.End + 0x1;
	    	public static final int CreatedBy                         = ObjectFields.End + 0x2;
	    	public static final int End                               = ObjectFields.End + 0x4;
	    };

	    public class SceneObjectDynamicArrays
	    {
	    	 // Empty
	    	public static final int  End                               = DynamicObjectArrays.End;
	    }

}